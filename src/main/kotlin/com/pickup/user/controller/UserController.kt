package com.pickup.user.controller

import com.pickup.user.dto.UserResponse
import com.pickup.user.dto.UserSignUpRequest
import com.pickup.user.dto.UserUpdateRequest
import com.pickup.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
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
    fun signUpUser(
        @Valid @RequestBody signUpRequest: UserSignUpRequest,
        bindingResult: BindingResult
    ): ResponseEntity<UserResponse> {
        val newUser = userService.signUpUser(signUpRequest)
        return ResponseEntity.ok(newUser)
    }


    /**
     * 회원 정보 수정
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody updateRequest: UserUpdateRequest
    ): ResponseEntity<UserResponse> {
        val user = userService.updateUser(userId, updateRequest)
        return ResponseEntity.ok(user)
    }

    /**
     * 회원 조회
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserResponse> {
        val restaurant = userService.getUser(userId)
        return ResponseEntity.ok(restaurant)
    }

}