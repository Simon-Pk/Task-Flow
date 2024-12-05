package com.example.taskflow.data

data class Priority(val uid: String, val code: Int, val name: String, val value: Int) {
    constructor() : this(uid = "", code = 0, name = "", value = 0)
}
