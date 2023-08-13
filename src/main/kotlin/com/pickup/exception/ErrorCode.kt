package com.pickup.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus
) {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST),
    JWT_ERROR(HttpStatus.UNAUTHORIZED),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    DUPLICATE(HttpStatus.BAD_REQUEST),
    NOT_CHANGED(HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST);
}

