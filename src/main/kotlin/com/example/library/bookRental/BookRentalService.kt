package com.example.library.bookRental

import com.example.library.book.BookRepository
import com.example.library.book.ViewBook
import com.example.library.book.toView
import com.example.library.user.UserRepository
import com.example.library.user.ViewUser
import com.example.library.user.toView
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.sql.Date
import java.time.LocalDate

@Service
class BookRentalService(
    val bookRentalRepository: BookRentalRepository,
    val bookRepository: BookRepository,
    val userRepository: UserRepository
) {
    fun findAllBookRentals(): Iterable<ViewBookRental> =
        bookRentalRepository.findAll().map { it.toView() }

    fun findRentalById(rentalId: Long): ViewBookRental =
        bookRentalRepository
            .findById(rentalId)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such book rental.")
            }.toView()

    fun findMostPopularBook(
        startDate: Date?,
        endDate: Date?
    ): ViewBook {
        val popularBookId = if (startDate == null || endDate == null) {
            bookRentalRepository.findMostPopularBookId()
                .orElseThrow {
                    ResponseStatusException(HttpStatus.NOT_FOUND, "The books were not rented.")
                }
        } else {
            bookRentalRepository
                .findMostPopularBookIdWithRentDateBetween(
                    startDate = startDate,
                    endDate = endDate
                )
                .orElseThrow {
                    ResponseStatusException(HttpStatus.BAD_REQUEST, "No books were rented during this period.")
                }
        }

        return bookRepository.findById(
            popularBookId
        ).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such book.")
        }.toView()
    }

    fun findMostActiveUser(
        startDate: Date?,
        endDate: Date?
    ): ViewUser {
        val activeUserId = if (startDate == null || endDate == null) {
            bookRentalRepository.findMostActiveUserId()
                .orElseThrow {
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Nobody rented books.")
                }
        } else {
            bookRentalRepository
                .findMostActiveUserIdWithRentDateBetween(
                    startDate = startDate,
                    endDate = endDate
                )
                .orElseThrow {
                    ResponseStatusException(HttpStatus.BAD_REQUEST, "Nobody rented books during this period.")
                }
        }

        return userRepository.findById(
            activeUserId
        ).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user.")
        }.toView()
    }

    fun rentBook(request: BookRentalRequest, bookId: Long): ViewBookRental {
        val user = userRepository
            .findById(request.userId)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user.")
            }
        val book = bookRepository
            .findById(bookId)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such book.")
            }
        if (book.bookRentals.any { it.returnDate == null }) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "This book is already rented out.")
        }
        return bookRentalRepository.save(
            BookRental(
                user = user,
                book = book,
                rentDate =
                request.rentDate ?: Date.valueOf(LocalDate.now())
            )
        ).toView()
    }

    fun returnBook(
        request: BookRentalRequest,
        bookId: Long
    ): ViewBookRental {
        val bookRental = bookRentalRepository
            .findByBookIdAndUserIdAndReturnDateNull(bookId, request.userId)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such rental.")
            }

        return bookRentalRepository.save(
            bookRental.copy(
                returnDate = Date.valueOf(LocalDate.now())
            )
        ).toView()
    }

    fun deleteAllBookRentals() {
        bookRentalRepository.deleteAll()
    }

    fun deleteBookRentalById(rentalId: Long) {
        bookRentalRepository.deleteById(rentalId)
    }
}