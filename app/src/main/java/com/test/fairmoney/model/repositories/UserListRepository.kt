package com.test.fairmoney.model.repositories

import com.test.fairmoney.model.local.dao.AppDao
import com.test.fairmoney.model.local.entities.User
import com.test.fairmoney.model.remote.ApiService
import javax.inject.Inject

class UserListRepository @Inject constructor(
    private val api: ApiService,
    private val dao: AppDao
) {
    suspend fun getUsers(isConnected: Boolean): Pair<List<User>, String?> {
        val local = getLocalUsers()
        return if (local.isEmpty()) {
            if (isConnected)
                getRemoteUsers()
            else Pair(first = emptyList(), second = "No Internet Connection")
        } else {
            Pair(first = local, second = null)
        }
    }

    private suspend fun getRemoteUsers(): Pair<List<User>, String?> {
        val result = api.getUsers()
        return if (result.isSuccessful) {
            if (result.body() != null && result.body()?.data != null && result.body()?.data?.isNotEmpty() == true) {
                dao.saveUsers(result.body()!!.data)
                Pair(first = result.body()!!.data, second = null)
            } else Pair(first = emptyList(), second = "No Data Returned from API")
        } else {
            Pair(first = emptyList(), second = result.errorBody().toString())
        }
    }

    private fun getLocalUsers(): List<User> {
        return dao.getUsers()
    }
}