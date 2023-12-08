package com.user.list.model.repositories

import androidx.annotation.OpenForTesting
import com.user.list.model.NetworkState
import com.user.list.model.local.dao.AppDao
import com.user.list.model.local.entities.User
import com.user.list.model.models.Result
import com.user.list.model.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OpenForTesting
class UserListRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: AppDao,
    private val network: NetworkState
): UserListRepository {
    override suspend fun getUsers(): Result<List<User>> {
        val local = getLocalUsers()
        return if (local.isEmpty()) {
            if (network.hasInternet())
                getRemoteUsers()
            else Result(errorMessage = "No Internet Connection")
        } else {
            Result(local)
        }
    }

    private suspend fun getRemoteUsers(): Result<List<User>> = withContext(Dispatchers.IO) {
        val result = api.getUsers()
        if (result.isSuccessful) {
            if (result.body() != null && result.body()?.data != null && result.body()?.data?.isNotEmpty() == true) {
                dao.saveUsers(result.body()!!.data)
                Result(result.body()!!.data)
            } else Result(errorMessage = "No Data Returned from API")
        } else {
            Result(errorMessage = result.errorBody()?.string())
        }
    }

    private suspend fun getLocalUsers(): List<User>  = withContext(Dispatchers.IO){
        dao.getUsers()
    }
}