package com.test.fairmoney.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

inline fun <reified T : ViewModel> Fragment.inject(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory)[T::class.java]
}

fun String.formatDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return try {
        val date = sdf.parse(this)
        if (date == null)
            ""
        else
            SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}