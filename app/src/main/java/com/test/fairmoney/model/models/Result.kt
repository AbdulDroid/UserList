package com.test.fairmoney.model.models

data class Result<T> (
    val data: T? = null,
    val errorMessage: String? = null
)