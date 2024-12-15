package com.example.taskflow.data

data class Notifications(
    val uid: String,
    val content: String,
    val userId: String,
    val date: String
) {
    constructor() :
        this(
            uid = "",
            content = "",
            userId = "",
            date = "",
        )
}
