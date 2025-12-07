package io.github.faustofan.admin.auth.domain.service

import io.github.faustofan.admin.auth.domain.model.LoginUser
import io.github.faustofan.admin.shared.infrastructure.cache.CacheKeys
import io.github.faustofan.admin.shared.infrastructure.cache.CacheService
import org.springframework.stereotype.Service
import java.time.Duration

/**
 * 登录用户缓存服务
 *
 * 统一管理登录用户的缓存逻辑，使用 CacheKeys 避免魔法字符串
 */
@Service
class LoginUserCacheService(
    private val cacheService: CacheService
) {
    companion object {
        private val LOGIN_USER_TTL = Duration.ofMinutes(30)
    }

    /**
     * 根据用户名和租户获取缓存的登录用户
     */
    fun getByUsernameAndTenant(
        username: String,
        tenantId: Long,
        loader: () -> LoginUser?
    ): LoginUser? {
        val cacheKey = CacheKeys.userLoginByUsername(tenantId, username)
        return cacheService.getOrLoad(cacheKey, LOGIN_USER_TTL, loader)
    }

    /**
     * 根据用户 ID 获取缓存的登录用户
     */
    fun getByUserId(userId: Long, loader: () -> LoginUser?): LoginUser? {
        val cacheKey = CacheKeys.userLogin(userId)
        return cacheService.getOrLoad(cacheKey, LOGIN_USER_TTL, loader)
    }

    /**
     * 清除用户所有登录相关缓存
     */
    fun evictByUserId(userId: Long) {
        cacheService.delete(CacheKeys.userLogin(userId))
        cacheService.delete(CacheKeys.userPermissions(userId))
        cacheService.delete(CacheKeys.userRoles(userId))
        cacheService.delete(CacheKeys.userMenus(userId))
    }

    /**
     * 清除用户名相关缓存
     */
    fun evictByUsername(username: String, tenantId: Long) {
        cacheService.delete(CacheKeys.userLoginByUsername(tenantId, username))
    }

    /**
     * 清除用户所有缓存（包括 ID 和用户名）
     */
    fun evictAll(userId: Long, username: String, tenantId: Long) {
        evictByUserId(userId)
        evictByUsername(username, tenantId)
    }
}

