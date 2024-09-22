package com.example.library.book

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI


@RestController
@RequestMapping("/books")
class BookController(
    val bookService: BookService
) {
    @GetMapping("/")
    fun findAll(): ResponseEntity<Iterable<ViewBook>> =
        ResponseEntity.ok(bookService.findAllBooks())

    @GetMapping("/{bookId}")
    fun findById(@PathVariable bookId: Long): ResponseEntity<ViewBook> =
        ResponseEntity.ok(bookService.findBookById(bookId))

    @GetMapping("/titles/{title}")
    fun findByTitleContaining(@PathVariable title: String): ResponseEntity<Iterable<ViewBook>> =
        ResponseEntity.ok(bookService.findBooksByTitleContaining(title))

    @GetMapping("/authors/{authorName}")
    fun findByAuthorContaining(@PathVariable authorName: String): ResponseEntity<Iterable<ViewBook>> =
        ResponseEntity.ok(bookService.findBooksByAuthorContaining(authorName))

    @PostMapping("/")
    fun createBook(@RequestBody request: BookRequest): ResponseEntity<ViewBook> {
        val createdBook = bookService.createBook(request)
        return ResponseEntity.created(URI("/books/${createdBook.id}")).body(createdBook)
    }

    @PatchMapping("/{bookId}")
    fun updateBook(
        @PathVariable bookId: Long,
        @RequestBody request: PatchBookRequest
    ): ResponseEntity<ViewBook> =
        ResponseEntity.ok(bookService.updateBook(bookId, request))

    @DeleteMapping("/")
    fun deleteAll() {
        bookService.deleteAllBooks()
    }

    @DeleteMapping("/{bookId}")
    fun deleteById(@PathVariable bookId: Long) {
        bookService.deleteBookById(bookId)
    }
}