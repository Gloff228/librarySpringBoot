package com.example.library.book

import java.sql.Date

data class BookRequest(
    val title: String,
    val author: String,
    val creationDate: Date
)

data class PatchBookRequest(
    val title: String = "",
    val author: String = "",
    val creationDate: Date? = null
)