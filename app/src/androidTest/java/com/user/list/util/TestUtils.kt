package com.user.list.util

import com.user.list.model.local.entities.FullUser
import com.user.list.model.local.entities.User

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

fun getUserWithData(): User {
    return user.copy(
        userId = "0F8JIqi4zwvb77FGz6Wt",
        lastName = "Fiedler",
        firstName = "Heinz-Georg",
        email = "heinz-georg.fiedler@example.com",
        title = "mr",
        userImage = "https://randomuser.me/api/portraits/men/81.jpg"
    )
}