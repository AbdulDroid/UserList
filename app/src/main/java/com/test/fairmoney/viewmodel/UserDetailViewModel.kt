package com.test.fairmoney.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.repositories.UserDetailRepository
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
    private val repo: UserDetailRepository
) : BaseViewModel() {

    private val _user = MutableLiveData<FullUser>()
    val user: LiveData<FullUser> = _user

    fun getUser(isConnected: Boolean, userId: String) {
        loadResult {
            val response = repo.getUser(isConnected, userId)
            if (response.first != null) {
                _user.postValue(response.first)
            } else {
                throw Exception(response.second ?: "An Error Occurred")
            }
        }
    }
}