package com.user.list.model.repositories

import androidx.annotation.OpenForTesting
import com.user.list.model.NetworkState
import com.user.list.model.local.dao.AppDao
import com.user.list.model.local.entities.FullUser
import com.user.list.model.models.Result
import com.user.list.model.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class UserDetailRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: AppDao,
    private val network: NetworkState
): UserDetailRepository {

    override suspend fun getUser(userId: String): Result<FullUser> {
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