package com.example.library

import com.example.library.book.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.mockito.Mockito.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.sql.Date

@WebMvcTest(BookController::class)
class BookControllerTest {
    @MockBean
    private lateinit var bookService: BookService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun findAllBooks() {
        val books = listOf(
            ViewBook(1, "Title1", "Author1", "2024-01-01", "Alan"),
            ViewBook(2, "Title2", "Author2", "2022-09-01", "")
        )

        given(bookService.findAllBooks()).willReturn(books)

        mockMvc.perform(get("/books/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("Title1"))
            .andExpect(jsonPath("$[0].author").value("Author1"))
            .andExpect(jsonPath("$[0].creationDate").value("2024-01-01"))
            .andExpect(jsonPath("$[0].rentingUser").value("Alan"))
            .andExpect(jsonPath("$[1].title").value("Title2"))
            .andExpect(jsonPath("$[1].author").value("Author2"))
            .andExpect(jsonPath("$[1].creationDate").value("2022-09-01"))
            .andExpect(jsonPath("$[1].rentingUser").value(""))
        verify(bookService, times(1)).findAllBooks()
    }

    @Test
    fun findById() {
        val books = listOf(
            ViewBook(1, "First Book", "Author1", "2024-01-01", "Alan"),
            ViewBook(2, "Second Book", "Author2", "2022-09-01", ""),
            ViewBook(3, "Third Book", "Author1", "2023-09-01", "Sam")
        )

        given(bookService.findBookById(1)).willReturn(books[0])

        mockMvc.perform(get("/books/{bookId}", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("First Book"))
            .andExpect(jsonPath("$.author").value("Author1"))
            .andExpect(jsonPath("$.creationDate").value("2024-01-01"))
            .andExpect(jsonPath("$.rentingUser").value("Alan"))
        verify(bookService, times(1)).findBookById(1)
    }

    @Test
    fun findByTitleContaining() {
        val books = listOf(
            ViewBook(1, "First Book", "Author1", "2024-01-01", "Alan"),
            ViewBook(2, "Second Book", "Author2", "2022-09-01", ""),
            ViewBook(3, "Third Book", "Author1", "2023-09-01", "Sam")
        )

        given(bookService.findBooksByTitleContaining("Second")).willReturn(listOf(books[1]))

        mockMvc.perform(get("/books/titles/{title}", "Second"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].title").value("Second Book"))
            .andExpect(jsonPath("$[0].author").value("Author2"))
            .andExpect(jsonPath("$[0].creationDate").value("2022-09-01"))
            .andExpect(jsonPath("$[0].rentingUser").value(""))
        verify(bookService, times(1)).findBooksByTitleContaining("Second")
    }

    @Test
    fun updateBook() {
        val postRequest = BookRequest(
            title = "Title",
            author = "Author",
            creationDate = Date.valueOf("2024-01-01")
        )

        val patchRequest = PatchBookRequest(
            title = "newTitle",
            author = "newAuthor"
        )

        val patchRequestJson = objectMapper.writeValueAsString(patchRequest)

        mockMvc.perform(
            patch("/books/{bookId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(patchRequestJson)
        )
            .andExpect(status().isOk)
    }
}