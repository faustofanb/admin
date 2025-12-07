package io.github.faustofan.admin.system.app.query

import io.github.faustofan.admin.cache.Cached
import io.github.faustofan.admin.common.api.PageRequestDTO
import io.github.faustofan.admin.system.dto.SysTenantSearchQuery
import io.github.faustofan.admin.system.dto.SysTenantView
import io.github.faustofan.admin.system.infra.repository.SysTenantRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

/**
 * 租户查询服务
 *
 * 职责：处理租户的查询操作
 * 原则：使用声明式缓存注解，保持代码简洁
 */
@Service
@Transactional(readOnly = true)
class SysTenantQueryService(
    private val tenantRepository: SysTenantRepository
) {

    /**
     * 根据租户 ID 查询租户信息
     */
    @Cached(
        key = "T(io.github.faustofan.admin.cache.CacheKeys).tenant(#id)",
        ttl = 1,
        unit = TimeUnit.HOURS,
        cacheNull = true,
        nullTtlSeconds = 60
    )
    fun findById(id: Long): SysTenantView? {
        return tenantRepository.findNullable(id)?.let { SysTenantView(it) }
    }

    /**
     * 分页查询租户列表
     */
    fun search(query: SysTenantSearchQuery, page: PageRequestDTO): Page<SysTenantView> {
        return tenantRepository.findPage(query, page)
    }
}

