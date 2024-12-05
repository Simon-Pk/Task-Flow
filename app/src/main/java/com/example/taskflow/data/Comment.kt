package com.example.taskflow.data

data class Comment(
    val uid: String,
    val content: String,
    val user: String,
) {
    constructor() : this(uid = "", content = "", user = "")
}
