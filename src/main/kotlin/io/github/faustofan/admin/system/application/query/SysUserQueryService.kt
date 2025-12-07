package io.github.faustofan.admin.system.application.query

import io.github.faustofan.admin.shared.application.dto.PageRequestDTO
import io.github.faustofan.admin.shared.infrastructure.cache.Cached
import io.github.faustofan.admin.system.dto.SysUserSearchQuery
import io.github.faustofan.admin.system.dto.SysUserView
import io.github.faustofan.admin.system.infrastructure.persistence.SysUserRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

/**
 * 用户查询服务
 *
 * 职责：处理用户的查询操作
 * 原则：使用声明式缓存注解，保持代码简洁
 */
@Service
@Transactional(readOnly = true)
class SysUserQueryService(
    private val userRepository: SysUserRepository
) {

    /**
     * 根据 ID 查询用户详情
     *
     * 使用 SpEL 表达式调用 CacheKeys 生成缓存 Key
     */
    @Cached(
        key = "T(io.github.faustofan.admin.cache.CacheKeys).user(#id)",
        ttl = 15,
        unit = TimeUnit.MINUTES,
        cacheNull = true,
        nullTtlSeconds = 60
    )
    fun findById(id: Long): SysUserView? {
        return userRepository.findNullable(id)?.let { SysUserView(it) }
    }

    /**
     * 分页查询用户列表
     *
     * 列表查询通常不缓存，因为条件组合太多
     */
    fun search(query: SysUserSearchQuery, page: PageRequestDTO): Page<SysUserView> {
        return userRepository.findPage(query, page)
    }
}

