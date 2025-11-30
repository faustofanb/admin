package io.github.faustofan.admin.system.infra.repository

import io.github.faustofan.admin.system.domain.entity.SysMenu
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

@Repository
interface SysMenuRepository : KRepository<SysMenu, Long> {

    // 查询根菜单，并按排序字段排序
    // 注意：Jimmer 方法名支持 OrderBy
    fun findByParentIsNullOrderBySortOrder(): List<SysMenu>

    // 查询指定角色列表能看到的菜单
    // 这涉及到多对多关联查询 (SysMenu -> roles -> code)
    fun findByRolesCodeIn(roleCodes: Collection<String>): List<SysMenu>

    // 根据权限标识查询 (用于后端权限校验)
    fun findByPermission(permission: String): SysMenu?
}