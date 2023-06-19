package com.pickup.user.entity

import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Entity
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotEmpty
    @Length(min = 2, max = 50)
    val username: String,

    @NotEmpty
    @Email
    val email: String,

    @NotEmpty
    val password: String,

    @Enumerated(EnumType.STRING)
    val roles: Role = Role.USER,

    @NotEmpty
    @Column(unique = true)
    val phoneNumber: String,

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime = LocalDateTime.now()
)

enum class Role {
    USER,
    ADMIN,
    RESTAURANT_OWNER
}