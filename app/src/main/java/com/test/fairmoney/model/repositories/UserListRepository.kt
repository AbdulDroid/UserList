package com.test.fairmoney.model.repositories

import com.test.fairmoney.model.NetworkState
import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.local.entities.User
import com.test.fairmoney.model.models.Result
import com.test.fairmoney.model.remote.ApiService
import com.test.fairmoney.testing.OpenForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OpenForTesting
class UserListRepository @Inject constructor(
    private val api: ApiService,
    private val dao: AppDao,
    private val network: NetworkState
) {
    suspend fun getUsers(): Result<List<User>> {
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