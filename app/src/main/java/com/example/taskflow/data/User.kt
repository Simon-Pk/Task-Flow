package com.example.taskflow.data

data class User(val id: String, val name: String) {
    constructor() : this(id = "", name = "")
}
