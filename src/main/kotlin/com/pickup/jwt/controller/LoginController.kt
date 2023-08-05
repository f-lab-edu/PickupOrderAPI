package com.pickup.jwt.controller

import com.pickup.jwt.JwtTokenProvider
import com.pickup.jwt.dto.CustomUserDetails
import com.pickup.jwt.dto.LoginRequest
import com.pickup.jwt.service.CustomUserDetailsService
import com.pickup.util.Role
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/login")
class LoginController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager
) {
    @PostMapping
    fun login(@RequestBody loginRequest: LoginRequest, response: HttpServletResponse): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = authentication.details as CustomUserDetails
        return jwtTokenProvider.generateToken(userDetails.username, userDetails.getRole())
    }

}

