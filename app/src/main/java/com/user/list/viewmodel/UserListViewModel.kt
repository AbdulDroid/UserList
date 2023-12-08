package com.user.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.user.list.model.local.entities.User
import com.user.list.model.repositories.UserListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repo: UserListRepository
) : BaseViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun getUsers() {
        loadResult {
            val response = repo.getUsers()
             response.data?.let { users ->
                _users.postValue(users)
            } ?: run {
                throw Exception(response.errorMessage ?: "An Error Occurred")
            }
        }
    }
}