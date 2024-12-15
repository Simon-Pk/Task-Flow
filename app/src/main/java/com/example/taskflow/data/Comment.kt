package com.example.taskflow.data

data class Comment(
    val uid: String,
    val content: String,
    val taskId: String,
    val userId: String,
) {
    constructor() : this(uid = "", content = "", taskId = "", userId = "")
}
