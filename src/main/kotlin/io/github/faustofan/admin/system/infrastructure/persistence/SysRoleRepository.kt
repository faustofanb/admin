package io.github.faustofan.admin.system.infrastructure.persistence

import io.github.faustofan.admin.system.domain.model.SysRole
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

@Repository
interface SysRoleRepository : KRepository<SysRole, Long> {

    // 根据角色编码查询
//    fun findByCode(code: String): SysRole?

    // 查询某个用户的所有角色 (通过反向关联)
    // Jimmer 会自动生成 JOIN SQL
//    fun findByUsersUsername(username: String): List<SysRole>
}