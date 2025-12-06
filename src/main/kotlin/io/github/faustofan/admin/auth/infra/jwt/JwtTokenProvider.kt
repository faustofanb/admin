package io.github.faustofan.admin.auth.infra.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

/**
 * JWT Token 工具类
 * 负责 Token 的生成、解析、验证
 *
 * 使用 HMAC-SHA256 算法签名
 */
@Component
class JwtTokenProvider(
    @param:Value("\${app.jwt.secret:your-256-bit-secret-key-for-jwt-signing-please-change-in-production}")
    private val jwtSecret: String,

    @param:Value("\${app.jwt.access-token-expiration:3600000}") // 默认 1 小时
    private val accessTokenExpiration: Long,

    @param:Value("\${app.jwt.refresh-token-expiration:604800000}") // 默认 7 天
    private val refreshTokenExpiration: Long
) {

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    /**
     * 生成访问令牌
     */
    fun generateAccessToken(
        userId: Long,
        tenantId: Long,
        username: String,
        permissions: Set<String>
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .subject(userId.toString())
            .claim("tenantId", tenantId)
            .claim("username", username)
            .claim("permissions", permissions.joinToString(","))
            .claim("type", "access")
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    /**
     * 生成刷新令牌
     */
    fun generateRefreshToken(userId: Long, tenantId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshTokenExpiration)

        return Jwts.builder()
            .subject(userId.toString())
            .claim("tenantId", tenantId)
            .claim("type", "refresh")
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    /**
     * 从 Token 中获取用户 ID
     */
    fun getUserIdFromToken(token: String): Long {
        val claims = parseToken(token)
        return claims.subject.toLong()
    }

    /**
     * 从 Token 中获取租户 ID
     */
    fun getTenantIdFromToken(token: String): Long {
        val claims = parseToken(token)
        return (claims["tenantId"] as Number).toLong()
    }

    /**
     * 从 Token 中获取用户名
     */
    fun getUsernameFromToken(token: String): String {
        val claims = parseToken(token)
        return claims["username"] as String
    }

    /**
     * 获取 Token 类型（access 或 refresh）
     */
    fun getTokenType(token: String): String {
        val claims = parseToken(token)
        return claims["type"] as String
    }

    /**
     * 验证 Token 是否有效
     */
    fun validateToken(token: String): Boolean {
        return try {
            parseToken(token)
            true
        } catch (e: ExpiredJwtException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 检查 Token 是否过期
     */
    fun isTokenExpired(token: String): Boolean {
        return try {
            val claims = parseToken(token)
            claims.expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            true
        }
    }

    /**
     * 获取 Token 过期时间
     */
    fun getExpirationFromToken(token: String): Date {
        val claims = parseToken(token)
        return claims.expiration
    }

    /**
     * 解析 Token
     */
    private fun parseToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    /**
     * 获取 Access Token 过期时间（毫秒）
     */
    fun getAccessTokenExpiration(): Long = accessTokenExpiration

    /**
     * 获取 Refresh Token 过期时间（毫秒）
     */
    fun getRefreshTokenExpiration(): Long = refreshTokenExpiration
}

