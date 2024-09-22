package com.example.library.bookRental

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Date
import java.util.Optional

@Repository
interface BookRentalRepository : CrudRepository<BookRental, Long> {
    fun findByBookIdAndUserIdAndReturnDateNull(bookId: Long, userId: Long): Optional<BookRental>

    @Query(
        nativeQuery = true,
        value = "SELECT book_id FROM book_rental GROUP BY book_id ORDER BY COUNT(book_id) DESC LIMIT 1"
    )
    fun findMostPopularBookId(): Optional<Long>

    @Query(
        nativeQuery = true,
        value = "SELECT book_id FROM book_rental WHERE rent_date " +
                "BETWEEN :startDate AND :endDate GROUP BY book_id " +
                "ORDER BY COUNT(book_id) DESC LIMIT 1"
    )
    fun findMostPopularBookIdWithRentDateBetween(
        @Param("startDate") startDate: Date,
        @Param("endDate") endDate: Date
    ): Optional<Long>

    @Query(
        nativeQuery = true,
        value = "SELECT user_id FROM book_rental GROUP BY user_id ORDER BY COUNT(user_id) DESC LIMIT 1"
    )
    fun findMostActiveUserId(): Optional<Long>

    @Query(
        nativeQuery = true,
        value = "SELECT user_id FROM book_rental WHERE rent_date " +
                "BETWEEN :startDate AND :endDate GROUP BY user_id " +
                "ORDER BY COUNT(user_id) DESC LIMIT 1"
    )
    fun findMostActiveUserIdWithRentDateBetween(
        @Param("startDate") startDate: Date,
        @Param("endDate") endDate: Date
    ): Optional<Long>
}