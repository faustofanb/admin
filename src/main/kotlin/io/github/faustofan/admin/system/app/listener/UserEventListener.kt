package io.github.faustofan.admin.system.app.listener

import io.github.faustofan.admin.cache.CacheKeys
import io.github.faustofan.admin.cache.CacheService
import io.github.faustofan.admin.system.domain.entity.UserStatus
import io.github.faustofan.admin.system.domain.events.*
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
 * 用户领域事件监听器
 *
 * 负责监听用户相关的领域事件，处理缓存失效等副作用
 * 将缓存操作与业务逻辑解耦
 */
@Component
class UserEventListener(
    private val cacheService: CacheService
) {
    private val log = LoggerFactory.getLogger(UserEventListener::class.java)

    /**
     * 用户创建事件处理
     */
    @Async
    @EventListener
    fun onUserCreated(event: UserCreatedEvent) {
        log.info("处理用户创建事件: userId={}, username={}", event.userId, event.username)
        clearUserRelatedCache(event.userId)
    }

    /**
     * 用户更新事件处理
     */
    @Async
    @EventListener
    fun onUserUpdated(event: UserUpdatedEvent) {
        log.info("处理用户更新事件: userId={}, username={}", event.userId, event.username)
        clearUserRelatedCache(event.userId)
    }

    /**
     * 用户密码变更事件处理
     */
    @Async
    @EventListener
    fun onUserPasswordChanged(event: UserPasswordChangedEvent) {
        log.info("处理用户密码变更事件: userId={}", event.userId)
        clearUserLoginCache(event.userId)
    }

    /**
     * 用户状态变更事件处理
     */
    @Async
    @EventListener
    fun onUserStatusChanged(event: UserStatusChangedEvent) {
        log.info("处理用户状态变更事件: userId={}, {} -> {}",
            event.userId, event.oldStatus, event.newStatus)

        clearUserRelatedCache(event.userId)

        if (event.newStatus != UserStatus.ACTIVE) {
            clearUserLoginCache(event.userId)
        }
    }

    /**
     * 用户删除事件处理
     */
    @Async
    @EventListener
    fun onUserDeleted(event: UserDeletedEvent) {
        log.info("处理用户删除事件: userId={}, username={}", event.userId, event.username)
        clearUserRelatedCache(event.userId)
        clearUserLoginCache(event.userId)
    }

    /**
     * 批量用户删除事件处理
     */
    @Async
    @EventListener
    fun onUserBatchDeleted(event: UserBatchDeletedEvent) {
        log.info("处理批量用户删除事件: userIds={}", event.userIds)
        event.userIds.forEach { userId ->
            clearUserRelatedCache(userId)
            clearUserLoginCache(userId)
        }
    }

    /**
     * 清除用户相关缓存（基本信息、权限、角色、菜单）
     */
    private fun clearUserRelatedCache(userId: Long) {
        runCatching {
            cacheService.delete(CacheKeys.user(userId))
            cacheService.delete(CacheKeys.userPermissions(userId))
            cacheService.delete(CacheKeys.userRoles(userId))
            cacheService.delete(CacheKeys.userMenus(userId))
        }.onFailure { e ->
            log.error("清除用户缓存失败: userId={}", userId, e)
        }
    }

    /**
     * 清除用户登录相关缓存（Token、Session 等）
     */
    private fun clearUserLoginCache(userId: Long) {
        runCatching {
            cacheService.delete(CacheKeys.userLogin(userId))
        }.onFailure { e ->
            log.error("清除用户登录缓存失败: userId={}", userId, e)
        }
    }
}

