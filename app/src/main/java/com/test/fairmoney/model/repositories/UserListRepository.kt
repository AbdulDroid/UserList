package com.test.fairmoney.model.repositories

import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.local.entities.User
import com.test.fairmoney.model.remote.ApiService
import com.test.fairmoney.model.models.Result
import com.test.fairmoney.testing.OpenForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OpenForTesting
class UserListRepository @Inject constructor(
    private val api: ApiService,
    private val dao: AppDao
) {
    suspend fun getUsers(isConnected: Boolean): Result<List<User>> {
        val local = getLocalUsers()
        return if (local.isEmpty()) {
            if (isConnected)
                getRemoteUsers()
            else Result(errorMessage = "No Internet Connection")
        } else {
            Result(local)
        }
    }

    private suspend fun getRemoteUsers(): Result<List<User>> {
        val result = api.getUsers()
        return if (result.isSuccessful) {
            if (result.body() != null && result.body()?.data != null && result.body()?.data?.isNotEmpty() == true) {
                withContext(Dispatchers.IO) {
                    dao.saveUsers(result.body()!!.data)
                }
                Result(result.body()!!.data)
            } else Result(errorMessage = "No Data Returned from API")
        } else {
            withContext(Dispatchers.IO) {
                Result<List<User>>(errorMessage = result.errorBody()?.string())
            }
        }
    }

    private fun getLocalUsers(): List<User> {
        return dao.getUsers()
    }
}