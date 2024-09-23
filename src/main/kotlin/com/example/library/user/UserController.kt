package com.example.library.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService
) {
    @GetMapping("/")
    fun findAll(): ResponseEntity<Iterable<ViewUser>> =
        ResponseEntity.ok(userService.findAllUsers())

    @GetMapping("/{userId}")
    fun findById(@PathVariable userId: Long): ResponseEntity<ViewUser> =
        ResponseEntity.ok(userService.findUserById(userId))

    @GetMapping("/findByName/{userName}")
    fun findByNameContaining(@PathVariable userName: String): ResponseEntity<Iterable<ViewUser>> =
        ResponseEntity.ok(userService.findUsersByNameContaining(userName))

    @PostMapping("/")
    fun createUser(@RequestBody request: UserRequest): ResponseEntity<ViewUser> {
        val createdUser = userService.createUser(request)
        return ResponseEntity.created(URI("/users/${createdUser.id}")).body(createdUser)
    }

    @PatchMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: PatchUserRequest
    ): ResponseEntity<ViewUser> =
        ResponseEntity.ok(userService.updateUser(userId, request))

    @DeleteMapping("/")
    fun deleteAll(): ResponseEntity<Void> {
        userService.deleteAllUsers()
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{userId}")
    fun deleteById(@PathVariable userId: Long): ResponseEntity<Void> {
        userService.deleteUserById(userId)
        return ResponseEntity.noContent().build()
    }
}