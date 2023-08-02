package com.pickup.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(exception: UserAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.message)
    }

    @ExceptionHandler(value = [JwtAuthenticationException::class])
    fun handleJwtAuthenticationException(e: JwtAuthenticationException): ResponseEntity<String> {
        // JWT 인증에 관련된 예외를 처리합니다
        return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
    }

}