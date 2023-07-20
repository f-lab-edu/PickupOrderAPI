package com.pickup.user.dto

import com.pickup.user.entity.User
import java.time.LocalDateTime

data class UserResponse(
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    val joinedAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromUser(user: User): UserResponse {
            return UserResponse(
                nickname = user.nickname,
                email = user.email,
                phoneNumber = user.phoneNumber,
                joinedAt = user.joinedAt,
                updatedAt = user.updatedAt ?: user.joinedAt
            )
        }

    }
}


