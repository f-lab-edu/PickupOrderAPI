package com.pickup.exception

import org.springframework.http.HttpStatus

class JwtAuthenticationException(
    val errorMessage: ErrorMessage,
    cause: Throwable?
) : RuntimeException(errorMessage.message, cause) {

    fun getHttpStatus(): HttpStatus {
        return ErrorCode.JWT_ERROR.httpStatus
    }
}