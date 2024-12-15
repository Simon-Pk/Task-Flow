package com.example.taskflow.data

data class DataComment(
    val uid: String,
    val content: String,
    val taskId: String,
    val userId: User,
) {
    constructor() : this(uid = "", content = "", taskId = "", userId = User())
}
