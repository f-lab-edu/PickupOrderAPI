package com.pickup.jwt.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class LoginRequest(
    @field:Email(message = "유효한 이메일 주소를 입력하세요.")
    @field:NotBlank(message = "이메일 주소는 필수 입력 항목입니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @field:Size(min = 8, max = 20)
    val password: String,

    )
