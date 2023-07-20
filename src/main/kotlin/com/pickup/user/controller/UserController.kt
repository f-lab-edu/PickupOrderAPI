package com.pickup.user.controller

import com.pickup.user.dto.UserResponse
import com.pickup.user.dto.UserSignUpRequest
import com.pickup.user.dto.UserUpdateRequest
import com.pickup.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
@Validated
class UserController(private val userService: UserService) {

    /**
     * 회원 가입
     */
    @PostMapping("/signUp")
    fun signUpUser(@Valid @RequestBody userDto: UserSignUpRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.signUpUser(userDto))
    }


    /**
     * 회원 수정
     */
    @PutMapping("/{userId}")
    fun updateUser(@PathVariable userId: Long, @RequestBody updateDto: UserUpdateRequest): ResponseEntity<UserResponse> {
        val user = userService.updateUser(userId, updateDto)
        return ResponseEntity.ok(UserResponse.fromUser(user))
    }

}