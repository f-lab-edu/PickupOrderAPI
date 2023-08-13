package com.pickup.exception

import org.springframework.http.HttpStatus

class ValidationErrorResponse(
    val errors: List<String?>
) {

    fun getHttpStatus(): HttpStatus {
        return ErrorCode.VALIDATION_ERROR.httpStatus

    }
}

