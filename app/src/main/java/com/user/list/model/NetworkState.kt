package com.user.list.model

interface NetworkState {
    suspend fun hasInternet(): Boolean
}