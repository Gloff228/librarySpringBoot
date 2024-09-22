package com.example.library.bookRental

import com.example.library.book.Book
import com.example.library.user.User
import jakarta.persistence.*
import java.sql.Date

@Entity
data class BookRental(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "book_id")
    val book: Book,

    val rentDate: Date,
    val returnDate: Date? = null
)

data class ViewBookRental(
    val id: Long,
    val userName: String,
    val bookTitle: String,
    val rentDate: String,
    val returnDate: String
)

fun BookRental.toView() =
    ViewBookRental(
        id,
        user.name,
        book.title,
        rentDate.toString(),
        returnDate?.toString() ?: ""
    )
