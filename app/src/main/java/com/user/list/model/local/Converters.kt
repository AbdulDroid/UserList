package com.user.list.model.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.user.list.model.local.entities.Location

class Converters {
    private val gSon = Gson()

    @TypeConverter
    fun locationToString(location: Location): String = gSon.toJson(location)

    @TypeConverter
    fun stringToLocation(value: String): Location = gSon.fromJson(value, Location::class.java)
}