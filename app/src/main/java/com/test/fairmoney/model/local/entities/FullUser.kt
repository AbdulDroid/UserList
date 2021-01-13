package com.test.fairmoney.model.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class FullUser(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    @SerializedName("picture")
    @Expose
    val userImage: String = "",
    val email: String = "",
    val title: String = "",
    val registerDate: String = "",
    @SerializedName("dateOfBirth")
    @Expose
    val dob: String = "",
    val gender: String = "",
    val location: Location = Location(),
    val phone: String = ""
)