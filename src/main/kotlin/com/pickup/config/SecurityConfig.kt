package com.pickup.config

import com.pickup.jwt.JwtTokenFilter
import com.pickup.jwt.JwtTokenProvider
import com.pickup.jwt.service.CustomUserDetailsService
import com.pickup.util.Role
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) {


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .authorizeRequests { authorizeRequests ->
                authorizeRequests
                    .antMatchers("/login").permitAll()
                    .antMatchers("/restaurant/**").hasRole(Role.RESTAURANT.name)
                    .antMatchers("/user/**").hasRole(Role.USER.name)
                    .anyRequest().authenticated()
            }
            .httpBasic().disable()
            .csrf().disable()
            .addFilterBefore(
                JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java
            )
            .formLogin().disable()
            .headers()
            .frameOptions().sameOrigin()

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return object : AuthenticationManager {
            override fun authenticate(authentication: Authentication): Authentication {
                val username = authentication.principal as String
                val password = authentication.credentials as String
                val userDetails = customUserDetailsService.loadUserByUsername(username)
                if (passwordEncoder().matches(password, userDetails.password)) {
                    return UsernamePasswordAuthenticationToken(
                        userDetails, password, userDetails.authorities
                    )
                } else {
                    throw BadCredentialsException("Invalid credentials")
                }
            }
        }
    }
}
