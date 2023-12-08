package com.user.list.model.models

data class Result<T> (
    val data: T? = null,
    val errorMessage: String? = null
)