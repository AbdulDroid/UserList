package com.test.fairmoney.model.repositories

import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.remote.ApiService
import javax.inject.Inject

class UserDetailRepository @Inject constructor(
    private val api: ApiService,
    private val dao: AppDao
){

    suspend fun getUser(isConnected: Boolean, userId: String): Pair<FullUser?, String?> {
        val local = getLocalUser(userId)
        return when {
            local != null -> Pair(first = local, second = null)
            isConnected -> getRemoteUser(userId)
            else -> Pair(first = null, second = "No Internet Connection")
        }
    }

    private suspend fun getRemoteUser(userId: String): Pair<FullUser?, String?> {
        val response = api.getUser(userId)
        return if(response.isSuccessful) {
            if(response.body() != null) {
                dao.saveFullUser(response.body()!!)
                Pair(first = response.body()!!, second = null)
            }
            else Pair(first = null, second = "No user found with ID: $userId")
        } else {
            Pair(first = null, second = response.errorBody()?.toString() ?: "An error occurred")
        }
    }

    private fun getLocalUser(userId: String): FullUser? {
        return dao.getUser(userId)
    }
}