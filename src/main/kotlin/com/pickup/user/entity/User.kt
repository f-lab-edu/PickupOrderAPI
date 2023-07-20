package com.pickup.user.entity

import com.pickup.user.dto.UserSignUpRequest
import com.pickup.user.dto.UserUpdateRequest
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, nullable = false, length = 20)
    var nickname: String,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(unique = true, nullable = false, length = 13)
    var phoneNumber: String,

    @CreatedDate
    @Column(nullable = false)
    val joinedAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun fromDto(dto: UserSignUpRequest, passwordEncoder: PasswordEncoder): User {
            return User(
                nickname = dto.nickname,
                email = dto.email,
                password = passwordEncoder.encode(dto.password),
                phoneNumber = dto.phoneNumber
            )
        }
    }

    fun updateFromDto(dto: UserUpdateRequest, passwordEncoder: PasswordEncoder) {
        dto.nickname?.let { this.nickname = it }
        dto.email?.let { this.email = it }
        dto.password?.let { this.password = passwordEncoder.encode(it) }
        dto.phoneNumber?.let { this.phoneNumber = it }
        this.updatedAt = LocalDateTime.now()
    }


}
