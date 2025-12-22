package io.github.faustofan.admin.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;

/**
 * JWT 令牌提供者
 * <p>
 * 负责生成、解析和校验 JWT 令牌，支持访问令牌和刷新令牌的生成与验证。
 * 依赖于 {@link SecurityProperties} 获取密钥和过期时间配置。
 */
@Component
public class JwtTokenProvider {

    /** 安全配置属性，包含密钥和过期时间等 */
    private final SecurityProperties securityProperties;
    /** JWT 签名密钥 */
    private final SecretKey secretKey;

    /** 请求头中存放 JWT 的字段名 */
    private static final String AUTHORIZATION_HEADER = "Authorization";
    /** JWT Token 前缀 */
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 构造方法，初始化密钥
     * @param securityProperties 安全配置属性
     */
    public JwtTokenProvider(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.secretKey = Keys.hmacShaKeyFor(securityProperties.getJwtSecret().getBytes());
    }

    /**
     * 生成访问令牌（Access Token）
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param username 用户名
     * @param permissions 权限集合
     * @return JWT 字符串
     */
    public String generateAccessToken(Long userId, Long tenantId, String username, Set<String> permissions) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + securityProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("tenantId", tenantId)
                .claim("username", username)
                .claim("permissions", String.join(",", permissions))
                .claim("type", "access")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 生成刷新令牌（Refresh Token）
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return JWT 字符串
     */
    public String generateRefreshToken(Long userId, Long tenantId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + securityProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("tenantId", tenantId)
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 从 Token 中获取用户 ID
     * @param token JWT 字符串
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 从 Token 中获取租户 ID
     * @param token JWT 字符串
     * @return 租户ID
     */
    public Long getTenantIdFromToken(String token) {
        Claims claims = parseToken(token);
        return ((Number) claims.get("tenantId")).longValue();
    }

    /**
     * 从 Token 中获取用户名
     * @param token JWT 字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("username");
    }

    /**
     * 获取 Token 类型（access 或 refresh）
     * @param token JWT 字符串
     * @return 类型字符串
     */
    public String getTokenType(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("type");
    }

    /**
     * 验证 Token 是否有效
     * @param token JWT 字符串
     * @return 有效返回 true，否则 false
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查 Token 是否过期
     * @param token JWT 字符串
     * @return 过期返回 true，否则 false
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 获取 Token 过期时间
     * @param token JWT 字符串
     * @return 过期时间
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * 解析 Token，获取 Claims
     * @param token JWT 字符串
     * @return Claims 对象
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取 Access Token 过期时间（毫秒）
     * @return 过期时间（毫秒）
     */
    public long getAccessTokenExpiration() {
        return securityProperties.getAccessTokenExpiration();
    }

    /**
     * 获取 Refresh Token 过期时间（毫秒）
     * @return 过期时间（毫秒）
     */
    public long getRefreshTokenExpiration() {
        return securityProperties.getRefreshTokenExpiration();
    }

    /**
     * 从请求头中提取 JWT Token
     *
     * @param request HTTP 请求
     * @return JWT Token 字符串，若不存在则返回 null
     */
    public String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}