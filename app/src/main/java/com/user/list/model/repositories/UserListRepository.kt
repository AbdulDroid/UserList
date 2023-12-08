package com.user.list.model.repositories

import com.user.list.model.local.entities.User
import com.user.list.model.models.Result


interface UserListRepository  {
    suspend fun getUsers(): Result<List<User>>
}