package com.pickup.jwt.controller

import com.pickup.jwt.JwtTokenProvider
import com.pickup.jwt.dto.LoginRequest
import com.pickup.jwt.service.CustomUserDetailsService
import com.pickup.util.Role
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/login")
class LoginController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) {

    @PostMapping
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<JwtAuthenticationResponse> {
        val userDetails = customUserDetailsService.loadUserByUsername(loginRequest.email)
        authenticate(loginRequest.email, loginRequest.password)
        val authority = userDetails.authorities.first()
        val authorityName = authority.authority
        val role = Role.valueOf(authorityName)
        val jwt = jwtTokenProvider.generateToken(loginRequest.email, role)

        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }

    private fun authenticate(email: String, password: String) {
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)
        authenticationManager.authenticate(authenticationToken)
    }
}

data class JwtAuthenticationResponse(
    val token: String
)