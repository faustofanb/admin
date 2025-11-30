package io.github.faustofan.admin.system.infra.repository

import io.github.faustofan.admin.system.domain.entity.SysUser
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

@Repository
interface SysUserRepository : KRepository<SysUser, Long> {

    // 根据用户名查询
    fun findByUsername(username: String): SysUser?

    // 根据手机号查询
    fun findByPhone(phone: String): SysUser?

    // 根据邮箱查询
    fun findByEmail(email: String): SysUser?
}