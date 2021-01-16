package com.test.fairmoney.model.repositories

import com.test.fairmoney.model.NetworkState
import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.models.Result
import com.test.fairmoney.model.remote.ApiService
import com.test.fairmoney.testing.OpenForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class UserDetailRepository @Inject constructor(
    private val api: ApiService,
    private val dao: AppDao,
    private val network: NetworkState
) {

    suspend fun getUser(userId: String): Result<FullUser> {
        val local = getLocalUser(userId)
        return when {
            local != null -> Result(local)
            network.hasInternet() -> getRemoteUser(userId)
            else -> Result(errorMessage = "No Internet Connection")
        }
    }

    private suspend fun getRemoteUser(userId: String): Result<FullUser> =
        withContext(Dispatchers.IO) {
            val response = api.getUser(userId)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    dao.saveFullUser(response.body()!!)
                    Result(response.body())
                } else Result(errorMessage = "No user found with ID: $userId")
            } else {
                Result(errorMessage = response.errorBody()?.string() ?: "An error occurred")
            }
        }

    private suspend fun getLocalUser(userId: String): FullUser? = withContext(Dispatchers.IO){
        dao.getUser(userId)
    }
}