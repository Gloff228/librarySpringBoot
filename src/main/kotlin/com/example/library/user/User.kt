package com.example.library.user

import com.example.library.bookRental.BookRental
import jakarta.persistence.*
import java.sql.Date

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val email: String,
    val registrationDate: Date,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val bookRentals: List<BookRental> = listOf()
)

data class ViewUser(
    val id: Long,
    val name: String,
    val email: String,
    val registrationDate: String,
    val takenBooks: List<String>
)

fun User.toView() =
    ViewUser(
        id,
        name,
        email,
        registrationDate.toString(),
        bookRentals
            .filter { it.returnDate == null }
            .map { it.book.title }
    )