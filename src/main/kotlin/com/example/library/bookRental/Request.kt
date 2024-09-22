package com.example.library.bookRental

import java.sql.Date

data class BookRentalRequest(
    val userId: Long,
    val rentDate: Date? = null
)