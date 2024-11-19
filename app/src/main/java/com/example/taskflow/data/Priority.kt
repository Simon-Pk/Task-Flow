package com.example.taskflow.data

data class Priority(val id: Int, val name: String, val value: Int) {
    constructor() : this(id = 0, name = "", value = 0)
}
