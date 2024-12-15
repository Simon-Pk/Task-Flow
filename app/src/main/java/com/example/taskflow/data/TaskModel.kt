package com.example.taskflow.data

data class TaskModel(
    val uid: String,
    val title: String,
    val statusId: String,
    val content: String,
    val executorId: String,
    val finishDate: String,
    val priorityId: String,
    val startDate: String
) {
    constructor() :
        this(
            uid = "",
            title = "",
            statusId = "",
            content = "",
            executorId = "",
            finishDate = "",
            priorityId = "",
            startDate = ""
        )
}
