package io.github.faustofan.admin.system.infra.repository

import io.github.faustofan.admin.system.domain.entity.SysTenant
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

@Repository
interface SysTenantRepository : KRepository<SysTenant, Long> {

    // 根据租户编码查询 (通常用于登录时解析租户)
    fun findByCode(code: String): SysTenant?
}