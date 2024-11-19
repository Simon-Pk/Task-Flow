package com.example.taskflow.Tools

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.getValue
import java.text.SimpleDateFormat
import java.util.Date

inline fun <reified T> Iterable<DataSnapshot>.convertToClass(): List<T> {
    val data = mutableListOf<T>()
    this.forEach {
        val item = it.getValue<T>()
        if (item !== null) {
            data.add(item)
        }
    }
    return data
}

fun convertLongToDate(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd.MM.yyyy")
    return format.format(date)
}
