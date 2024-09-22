package com.example.library.book

import com.example.library.bookRental.BookRental
import jakarta.persistence.*
import java.sql.Date

@Entity
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String,
    val author: String,
    val creationDate: Date,
    @OneToMany(mappedBy = "book", cascade = [CascadeType.ALL])
    val bookRentals: List<BookRental> = listOf()
)

data class ViewBook(
    val id: Long,
    val title: String,
    val author: String,
    val creationDate: String,
    val rentingUser: String
)

fun Book.toView() =
    ViewBook(
        id,
        title,
        author,
        creationDate.toString(),
        bookRentals
            .find { it.returnDate == null }
            ?.user?.name ?: ""
    )