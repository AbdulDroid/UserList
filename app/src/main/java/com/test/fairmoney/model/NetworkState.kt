package com.test.fairmoney.model

interface NetworkState {
    suspend fun hasInternet(): Boolean
}