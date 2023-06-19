package com.pickup.user.controller

import com.pickup.user.entity.User
import com.pickup.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@Validated @RequestBody user: User): ResponseEntity<User> {
        return ResponseEntity.ok(userService.registerUser(user))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}