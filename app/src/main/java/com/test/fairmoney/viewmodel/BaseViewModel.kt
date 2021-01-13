package com.test.fairmoney.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    protected fun loadResult(block: suspend () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        try {
            _loading.postValue(true)
            block()
        } catch (e: Exception) {
            Log.e("ViewModel", e.message, e)
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