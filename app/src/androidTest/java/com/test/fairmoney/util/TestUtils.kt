package com.test.fairmoney.util

import com.test.fairmoney.model.local.entities.FullUser
import com.test.fairmoney.model.local.entities.User

val user = User()
val fullUser = FullUser()

fun getUsers(count: Int): List<User> {
    val list = arrayListOf<User>()
    for (i in 0 until count) {
        list.add(user.copy(userId = i.toString()))
    }
    return list
}

fun getUser(id: String): FullUser {
    return fullUser.copy(userId = id)
}