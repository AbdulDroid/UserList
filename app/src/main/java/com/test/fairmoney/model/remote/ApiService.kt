package com.test.fairmoney.model.remote

import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.models.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("user?limit=100")
    suspend fun getUsers(): Response<UserResponse>

    @GET("user/{userId}")
    suspend fun getUser(@Path("userId") id: String): Response<FullUser>
}