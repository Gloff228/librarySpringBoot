package com.example.library.book

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CrudRepository<Book, Long> {
    fun findByTitleContaining(title: String): Iterable<Book>
    fun findByAuthorContaining(authorName: String): Iterable<Book>
}