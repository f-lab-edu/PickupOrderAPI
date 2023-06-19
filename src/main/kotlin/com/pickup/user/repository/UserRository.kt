package com.pickup.user.repository

import com.pickup.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByPhoneNumber(phoneNumber: String): Optional<User>
}