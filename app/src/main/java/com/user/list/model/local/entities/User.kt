package com.user.list.model.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User (
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
    val title: String = ""
): Parcelable