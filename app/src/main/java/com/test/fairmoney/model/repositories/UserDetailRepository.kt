package com.test.fairmoney.model.repositories

import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.models.Result
import com.test.fairmoney.model.remote.ApiService
import com.test.fairmoney.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class UserDetailRepository @Inject constructor(
    private val api: ApiService,
    private val dao: AppDao
) {

    suspend fun getUser(isConnected: Boolean, userId: String): Result<FullUser> {
        val local = getLocalUser(userId)
        return when {
            local != null -> Result(local)
            isConnected -> getRemoteUser(userId)
            else -> Result(errorMessage = "No Internet Connection")
        }
    }

    private suspend fun getRemoteUser(userId: String): Result<FullUser> {
        val response = api.getUser(userId)
        return if (response.isSuccessful) {
            if (response.body() != null) {
                dao.saveFullUser(response.body()!!)
                Result(response.body())
            } else Result(errorMessage = "No user found with ID: $userId")
        } else {
            Result(errorMessage = response.errorBody()?.string() ?: "An error occurred")
        }
    }

    private fun getLocalUser(userId: String): FullUser? {
        return dao.getUser(userId)
    }
}