package com.example.library.user

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.sql.Date
import java.time.LocalDate

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun findAllUsers(): Iterable<ViewUser> =
        userRepository.findAll().map { it.toView() }

    fun findUserById(userId: Long): ViewUser =
        userRepository
            .findById(userId)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user.")
            }.toView()

    fun findUsersByNameContaining(userName: String): Iterable<ViewUser> =
        userRepository.findByNameContaining(userName).map { it.toView() }

    fun createUser(userRequest: UserRequest): ViewUser =
        userRepository.save(
            User(
                name = userRequest.name,
                email = userRequest.email,
                registrationDate =
                userRequest.registrationDate.takeIf { it != null } ?: Date.valueOf(LocalDate.now())
            )
        ).toView()

    fun updateUser(
        userId: Long,
        patchUserRequest: PatchUserRequest
    ): ViewUser {
        val user = userRepository
            .findById(userId)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user.")
            }
        return userRepository.save(
            user.copy(
                name = patchUserRequest.name.takeIf { it.isNotBlank() } ?: user.name,
                email = patchUserRequest.email.takeIf { it.isNotBlank() } ?: user.email,
                registrationDate = patchUserRequest.registrationDate.takeIf { it != null } ?: user.registrationDate
            )
        ).toView()
    }

    fun deleteAllUsers() {
        userRepository.deleteAll()
    }

    fun deleteUserById(userId: Long) {
        userRepository.deleteById(userId)
    }
}