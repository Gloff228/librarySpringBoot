package com.example.library.user

import java.sql.Date

data class UserRequest(
    val name: String,
    val email: String,
    val registrationDate: Date? = null
)

data class PatchUserRequest(
    val name: String = "",
    val email: String = "",
    val registrationDate: Date? = null
)