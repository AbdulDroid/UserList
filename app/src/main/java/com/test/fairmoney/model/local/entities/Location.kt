package com.test.fairmoney.model.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val state: String = "",
    val street: String = "",
    val city: String = "",
    val country: String = ""
)