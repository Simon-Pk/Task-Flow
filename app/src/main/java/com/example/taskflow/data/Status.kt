package com.example.taskflow.data

data class Status(val uid: String, val name: String, val code: Int) {
    constructor() : this(uid = "", name = "", code = 0)
}
