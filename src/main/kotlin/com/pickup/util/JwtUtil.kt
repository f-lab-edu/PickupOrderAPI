package com.pickup.util
import com.pickup.jwt.dto.CustomUserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    // JWT 생성
    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()

        if (userDetails is CustomUserDetails) {
            claims["role"] = userDetails.authorities.map { it.authority }.joinToString(",")
        }

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10시간 만료
            .signWith(SignatureAlgorithm.HS256, secret)  // HS256과 secret key를 이용하여 Signature 부분 생성
            .compact()
    }


    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token) { it.subject }
    }

    // 토큰의 만료 여부 확인
    fun isTokenExpired(token: String): Boolean {
        return getClaimFromToken(token) { it.expiration }.before(Date())
    }

    // Claim 추출
    fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    // JWT로부터 Claim 전체를 얻음
    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    }
}
