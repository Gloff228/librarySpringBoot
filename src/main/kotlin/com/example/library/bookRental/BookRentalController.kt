package com.example.library.bookRental

import com.example.library.book.ViewBook
import com.example.library.user.ViewUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.sql.Date


@RestController
@RequestMapping("/rentals")
class BookRentalController(
    val bookRentalService: BookRentalService
) {
    @GetMapping("/")
    fun findAll(): ResponseEntity<Iterable<ViewBookRental>> =
        ResponseEntity.ok(bookRentalService.findAllBookRentals())

    @GetMapping("/{rentalId}")
    fun findRentalById(@PathVariable rentalId: Long): ResponseEntity<ViewBookRental> =
        ResponseEntity.ok(bookRentalService.findRentalById(rentalId))

    @GetMapping("/popularBook")
    fun findMostPopularBook(
        @RequestParam("startDate") startDate: Date? = null,
        @RequestParam("endDate") endDate: Date? = null
    ): ResponseEntity<ViewBook> =
        ResponseEntity.ok(bookRentalService.findMostPopularBook(startDate, endDate))

    @GetMapping("/activeUser")
    fun findMostActiveUser(
        @RequestParam("startDate") startDate: Date? = null,
        @RequestParam("endDate") endDate: Date? = null
    ): ResponseEntity<ViewUser> =
        ResponseEntity.ok(bookRentalService.findMostActiveUser(startDate, endDate))

    @PostMapping("/rent/{bookId}")
    fun rentBook(
        @RequestBody request: BookRentalRequest,
        @PathVariable bookId: Long
    ): ResponseEntity<ViewBookRental> {
        val createdBookRental = bookRentalService.rentBook(request, bookId)
        return ResponseEntity.created(URI("/rentals/${createdBookRental.id}")).body(createdBookRental)
    }

    @PatchMapping("/return/{bookId}")
    fun returnBook(
        @RequestBody request: BookRentalRequest,
        @PathVariable bookId: Long
    ): ResponseEntity<ViewBookRental> =
        ResponseEntity.ok(bookRentalService.returnBook(request, bookId))

    @DeleteMapping("/")
    fun deleteAll() {
        bookRentalService.deleteAllBookRentals()
    }

    @DeleteMapping("/{rentalId}")
    fun deleteById(@PathVariable rentalId: Long) {
        bookRentalService.deleteBookRentalById(rentalId)
    }
}