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

    fun getUser(userId: String) {
        loadResult {
            val response = repo.getUser(userId)
            if (response.data != null) {
                _user.postValue(response.data)
            } else {
                throw Exception(response.errorMessage ?: "An Error Occurred")
            }
        }
    }
}