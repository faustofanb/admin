package io.github.faustofanb.admin.module.system.service;

import io.github.faustofanb.admin.module.system.model.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenService {

    // Header key
    private static final String HEADER = "Authorization";
    // Token prefix
    private static final String TOKEN_PREFIX = "Bearer ";
    // Redis key prefix
    private static final String LOGIN_TOKEN_KEY = "login_tokens:";

    // Secret key (should be at least 256 bits)
    @Value("${token.secret:abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz}")
    private String secret;

    // Expiration time (minutes)
    @Value("${token.expireTime:60}")
    private int expireTime;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Get LoginUser from request
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            try {
                Claims claims = parseToken(token);
                String uuid = (String) claims.get("uuid");
                String userKey = LOGIN_TOKEN_KEY + uuid;
                return (LoginUser) redisTemplate.opsForValue().get(userKey);
            } catch (Exception e) {
                // Token invalid or expired
            }
        }
        return null;
    }

    /**
     * Create token
     */
    public String createToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();
        loginUser.setUserId(loginUser.getUser().id()); // Ensure ID is set
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", token);
        return createToken(claims);
    }

    /**
     * Verify and refresh token
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= 20 * 60 * 1000L) {
            refreshToken(loginUser);
        }
    }

    /**
     * Refresh token validity
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * 60 * 1000L);
        String userKey = getTokenKey(loginUser.getToken());
        redisTemplate.opsForValue().set(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * Create JWT
     */
    private String createToken(Map<String, Object> claims) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Parse JWT
     */
    private Claims parseToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get token from request
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return LOGIN_TOKEN_KEY + uuid;
    }

    public void delLoginUser(String token) {
        if (StringUtils.hasText(token)) {
            String userKey = getTokenKey(token);
            redisTemplate.delete(userKey);
        }
    }
}
