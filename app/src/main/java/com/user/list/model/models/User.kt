package com.user.list.model.models

import com.user.list.model.local.entities.User

data class UserResponse(
    val data: List<User> = emptyList(),
    val total: Int = 0,
    val page: Int = 0,
    val limit: Int = 0,
    val offset: Int = 0
)