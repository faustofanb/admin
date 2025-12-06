package io.github.faustofan.admin.system.infra.repository

import io.github.faustofan.admin.common.api.PageRequestDTO
import io.github.faustofan.admin.system.domain.entity.*
import io.github.faustofan.admin.system.dto.SysUserSearchQuery
import io.github.faustofan.admin.system.dto.SysUserView
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.spring.repository.fetchSpringPage
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface SysUserRepository : KRepository<SysUser, Long> {

    /**
     * 用于登录认证的 Fetcher
     * 需要获取用户的角色、菜单、权限等完整信息
     */
    companion object {
        val FETCHER_FOR_LOGIN: Fetcher<SysUser> = newFetcher(SysUser::class).by {
            allScalarFields()
            org {
                allScalarFields()
            }
            roles {
                allScalarFields()
                menus {
                    allScalarFields()
                }
                permissions {
                    allScalarFields()
                }
                specificOrgs ()
            }
        }
    }

    /**
     * 根据用户名和租户ID查询用户（用于登录认证）
     * 绕过租户过滤器，直接按租户ID查询
     */
    fun findByUsernameWithRoles(username: String, tenantId: Long): SysUser? {
        return sql.createQuery(SysUser::class) {
            where(table.username eq username)
            where(table.tenantId eq tenantId)
            where(table.status eq UserStatus.ACTIVE)
            select(table.fetch(FETCHER_FOR_LOGIN))
        }.fetchOneOrNull()
    }

    /**
     * 根据用户ID查询用户（用于 Token 刷新）
     */
    fun findByIdWithRoles(userId: Long): SysUser? {
        return sql.createQuery(SysUser::class) {
            where(table.id eq userId)
            select(table.fetch(FETCHER_FOR_LOGIN))
        }.fetchOneOrNull()
    }

    /**
     * 分页查询用户列表
     */
    fun findPage(query: SysUserSearchQuery, page: PageRequestDTO): Page<SysUserView> {
        return sql.createQuery(SysUser::class) {
            where(query)
            select(table.fetch(SysUserView::class))
        }.fetchSpringPage(page.toPageable())
    }

    /**
     * 检查用户名是否已存在
     */
    fun existsByUsernameAndTenantId(username: String, tenantId: Long): Boolean {
        return sql.createQuery(SysUser::class) {
            where(table.username eq username)
            where(table.tenantId eq tenantId)
            select(table.id)
        }.fetchOneOrNull() != null
    }
}