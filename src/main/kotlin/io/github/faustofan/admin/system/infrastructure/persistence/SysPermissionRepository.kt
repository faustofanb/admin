package io.github.faustofan.admin.system.infrastructure.persistence

import io.github.faustofan.admin.system.domain.model.SysPermission
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

@Repository
interface SysPermissionRepository : KRepository<SysPermission, Long> {

    // 根据权限编码查询 (如 "user:add")
//    fun findByCode(code: String): SysPermission?

    // 查询某个用户拥有的所有权限
    // 路径: SysPermission -> roles -> users -> username
//    fun findDistinctByRolesUsersUsername(username: String): List<SysPermission>
}