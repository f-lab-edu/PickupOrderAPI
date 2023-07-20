package com.pickup.user.dto

data class UserUpdateRequest(
    var nickname: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phoneNumber: String? = null
)
