package com.pickup.jwt.controller


import com.pickup.jwt.JwtTokenProvider
import com.pickup.jwt.dto.CustomUserDetails
import com.pickup.jwt.dto.LoginRequest
import com.pickup.util.Role
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.modelmapper.ModelMapper
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException


@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
private class LoginControllerTest {

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @InjectMocks
    private lateinit var loginController: LoginController

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var modelMapper: ModelMapper

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(LoginController(jwtTokenProvider, authenticationManager)).build()
    }


    @Test
    fun 로그인_성공() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val loginRequest = LoginRequest(email, password)

        val role = Role.USER
        val userDetails = CustomUserDetails(email, password, Role.USER)

        val authentication = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)

        authentication.details = userDetails

        val jwtToken = "jwt_token"

        // When
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenReturn(authentication)
        `when`(jwtTokenProvider.generateToken(email, role)).thenReturn(jwtToken)

        // Then
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "test@example.com", "password": "password"}""")
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(jwtToken))
    }

    @Test
    fun 로그인_실패() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val wrongPassword = "wrongpassword"
        val loginRequest = LoginRequest(email, wrongPassword)

        // When
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenThrow(BadCredentialsException("Invalid credentials"))

        // Then
        var actualException: Throwable? = null
        try {
            mockMvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"email": "$loginRequest.email", "password": "${loginRequest.password}wrongPassword"}""")
            )
        } catch (ex: NestedServletException) {
            actualException = ex.cause
        }

        assertTrue(actualException is BadCredentialsException)
    }


}