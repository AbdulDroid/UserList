package com.user.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    protected fun loadResult(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            _loading.postValue(true)
            block()
        } catch (e: Exception) {
            _error.postValue(
                if (e.message?.contains("No address associated with hostname") == true)
                    "Check Internet Connection"
                else e.message
            )
        } finally {
            _loading.postValue(false)
        }
    }
}