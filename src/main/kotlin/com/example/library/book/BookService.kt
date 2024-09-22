package com.example.library.book

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class BookService(
    val bookRepository: BookRepository
) {
    fun findAllBooks(): Iterable<ViewBook> =
        bookRepository.findAll().map { it.toView() }

    fun findBookById(bookId: Long): ViewBook =
        bookRepository.findById(bookId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such book.")
        }.toView()

    fun findBooksByTitleContaining(title: String): Iterable<ViewBook> =
        bookRepository.findByTitleContaining(title).map { it.toView() }

    fun findBooksByAuthorContaining(authorName: String): Iterable<ViewBook> =
        bookRepository.findByAuthorContaining(authorName).map { it.toView() }

    fun createBook(request: BookRequest): ViewBook =
        bookRepository.save(
            Book(
                title = request.title,
                author = request.author,
                creationDate = request.creationDate
            )
        ).toView()

    fun updateBook(
        bookId: Long,
        request: PatchBookRequest
    ): ViewBook {
        val book = bookRepository
            .findById(bookId)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such book.")
            }
        return bookRepository.save(
            book.copy(
                title = request.title.takeIf { it.isNotBlank() } ?: book.title,
                author = request.author.takeIf { it.isNotBlank() } ?: book.author,
                creationDate = request.creationDate.takeIf { it != null } ?: book.creationDate
            )
        ).toView()
    }

    fun deleteAllBooks() {
        bookRepository.deleteAll()
    }

    fun deleteBookById(bookId: Long) {
        bookRepository.deleteById(bookId)
    }
}
