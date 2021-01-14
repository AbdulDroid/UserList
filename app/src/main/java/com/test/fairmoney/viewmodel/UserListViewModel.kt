package com.test.fairmoney.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.fairmoney.model.local.entities.User
import com.test.fairmoney.model.repositories.UserListRepository
import javax.inject.Inject

class UserListViewModel @Inject constructor(
    private val repo: UserListRepository
) : BaseViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun getUsers(isConnected: Boolean) {
        loadResult {
            val response = repo.getUsers(isConnected)
            if (response.data != null) {
                _users.postValue(response.data)
            } else {
                throw Exception(response.errorMessage ?: "An Error Occurred")
            }
        }
    }
}