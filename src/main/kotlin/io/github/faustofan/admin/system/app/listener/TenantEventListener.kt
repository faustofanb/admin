package io.github.faustofan.admin.system.app.listener

import io.github.faustofan.admin.cache.CacheKeys
import io.github.faustofan.admin.cache.CacheService
import io.github.faustofan.admin.system.domain.entity.TenantStatus
import io.github.faustofan.admin.system.domain.events.*
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
 * 租户领域事件监听器
 *
 * 负责监听租户相关的领域事件，处理缓存失效等副作用
 */
@Component
class TenantEventListener(
    private val cacheService: CacheService
) {
    private val log = LoggerFactory.getLogger(TenantEventListener::class.java)

    /**
     * 租户创建事件处理
     */
    @Async
    @EventListener
    fun onTenantCreated(event: TenantCreatedEvent) {
        log.info("处理租户创建事件: tenantId={}, name={}", event.tenantId, event.name)
        clearTenantListCache()
    }

    /**
     * 租户更新事件处理
     */
    @Async
    @EventListener
    fun onTenantUpdated(event: TenantUpdatedEvent) {
        log.info("处理租户更新事件: tenantId={}, name={}", event.tenantId, event.name)
        clearTenantCache(event.tenantId)
        clearTenantListCache()
    }

    /**
     * 租户状态变更事件处理
     */
    @Async
    @EventListener
    fun onTenantStatusChanged(event: TenantStatusChangedEvent) {
        log.info("处理租户状态变更事件: tenantId={}, {} -> {}",
            event.tenantId, event.oldStatus, event.newStatus)

        clearTenantCache(event.tenantId)
        clearTenantListCache()

        if (event.newStatus == TenantStatus.DISABLE) {
            clearTenantUserCaches(event.tenantId)
        }
    }

    /**
     * 租户续费事件处理
     */
    @Async
    @EventListener
    fun onTenantRenewed(event: TenantRenewedEvent) {
        log.info("处理租户续费事件: tenantId={}, daysAdded={}", event.tenantId, event.daysAdded)
        clearTenantCache(event.tenantId)
    }

    /**
     * 租户删除事件处理
     */
    @Async
    @EventListener
    fun onTenantDeleted(event: TenantDeletedEvent) {
        log.info("处理租户删除事件: tenantId={}, code={}", event.tenantId, event.code)
        clearTenantCache(event.tenantId)
        clearTenantListCache()
        clearTenantUserCaches(event.tenantId)
    }

    private fun clearTenantCache(tenantId: Long) {
        runCatching {
            cacheService.delete(CacheKeys.tenant(tenantId))
        }.onFailure { e ->
            log.error("清除租户缓存失败: tenantId={}", tenantId, e)
        }
    }

    private fun clearTenantListCache() {
        runCatching {
            cacheService.delete(CacheKeys.TENANT_LIST)
        }.onFailure { e ->
            log.error("清除租户列表缓存失败", e)
        }
    }

    private fun clearTenantUserCaches(tenantId: Long) {
        runCatching {
            cacheService.deleteByPattern(CacheKeys.tenantPattern(tenantId))
        }.onFailure { e ->
            log.error("清除租户用户缓存失败: tenantId={}", tenantId, e)
        }
    }
}

