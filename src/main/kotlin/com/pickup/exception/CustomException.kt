package com.pickup.exception

import org.springframework.http.HttpStatus

class CustomException(
    val errorCode: ErrorCode,
    val errorMessage: ErrorMessage
) : RuntimeException(errorMessage.message) {
    fun getHttpStatus(): HttpStatus {
        return errorCode.httpStatus
    }
}

