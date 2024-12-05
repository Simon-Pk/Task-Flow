package com.example.taskflow.data

data class TaskModel(
    val uid: String,
    val title: String,
    val status: String,
    val content: String,
    val executor: String,
    val finishDate: String,
    val priority: String,
    val startDate: String
) {
    constructor() :
        this(
            uid = "",
            title = "",
            status = "",
            content = "",
            executor = "",
            finishDate = "",
            priority = "",
            startDate = ""
        )
}
