package com.pickup.user.dto

import java.time.LocalDateTime

data class UserResponse(
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    val joinedAt: LocalDateTime,
    val updatedAt: LocalDateTime
)


