package com.example.taskflow.data

data class User(val uid: String, val name: String) {
    constructor() : this(uid = "", name = "")
}
