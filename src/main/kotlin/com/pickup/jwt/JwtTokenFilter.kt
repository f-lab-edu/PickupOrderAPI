package com.pickup.jwt

import com.pickup.exception.JwtAuthenticationException
import com.pickup.jwt.service.JwtTokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(private val jwtTokenProvider: JwtTokenProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val token = jwtTokenProvider.resolveToken(request)

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                val auth = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
        } catch (ex: Exception) {
            // 여기에서 원래 예외를 JwtAuthenticationException으로 감싸서 다시 던집니다.
            throw JwtAuthenticationException("JWT 토큰 처리 중 오류가 발생했습니다.", ex)
        }

        filterChain.doFilter(request, response)
    }
}
