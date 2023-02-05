package com.example.packandgo

data class TaskWithPhoto(
    var id: String = "",
    var checked: Boolean = false,
    var imageUrl: String? = null,
    var name: String? = null,
    var description: String? = null
)
