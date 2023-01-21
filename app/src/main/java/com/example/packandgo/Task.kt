package com.example.packandgo

data class Task(
    var id: String = "",
    var isChecked: Boolean = false,
    var name: String? = null,
    var description: String? = null,
)