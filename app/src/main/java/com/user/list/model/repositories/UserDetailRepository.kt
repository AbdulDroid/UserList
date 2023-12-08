package com.user.list.model.repositories

import com.user.list.model.local.entities.FullUser
import com.user.list.model.models.Result


interface UserDetailRepository {
    suspend fun getUser(userId: String): Result<FullUser>
}