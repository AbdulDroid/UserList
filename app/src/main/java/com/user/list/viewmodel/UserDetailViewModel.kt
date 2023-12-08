package com.user.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.user.list.model.local.entities.FullUser
import com.user.list.model.repositories.UserDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val repo: UserDetailRepository
) : BaseViewModel() {

    private val _user = MutableLiveData<FullUser>()
    val user: LiveData<FullUser> = _user

    fun getUser(userId: String) {
        loadResult {
            val response = repo.getUser(userId)
            response.data?.let { user ->
                _user.postValue(user)
            } ?: run {
                throw Exception(response.errorMessage ?: "An Error Occurred")
            }
        }
    }
}