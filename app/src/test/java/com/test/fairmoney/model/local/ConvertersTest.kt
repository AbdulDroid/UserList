package com.test.fairmoney.model.local

import com.google.gson.Gson
import com.test.fairmoney.model.local.entities.Location
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {
    private val locationStr = "{\"id\":0,\"state\":\"Rheinland-Pfalz\",\"street\":\"4118, Schulstraße\",\"city\":\"Fellbach\",\"country\":\"Germany\"}"
    private val gSon = Gson()
    private val location = gSon.fromJson(locationStr, Location::class.java)
    @Test
    fun locationToString() {
        assertEquals(locationStr, Converters().locationToString(location))
    }
    @Test
    fun stringToLocation() {
        assertEquals(location, Converters().stringToLocation(locationStr))
    }
}