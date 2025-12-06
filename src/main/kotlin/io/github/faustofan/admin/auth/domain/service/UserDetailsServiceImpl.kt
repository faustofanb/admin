package io.github.faustofan.admin.auth.domain.service

import io.github.faustofan.admin.auth.domain.entity.LoginUser
import io.github.faustofan.admin.common.exception.BizException
import io.github.faustofan.admin.common.context.RoleDataScopeInfo
import io.github.faustofan.admin.system.domain.entity.*
import io.github.faustofan.admin.system.infra.repository.SysUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * 用户详情服务
 * 实现 Spring Security 的 UserDetailsService 接口
 * 用于登录时加载用户信息
 */
@Service
class UserDetailsServiceImpl(
    private val userRepository: SysUserRepository
) : UserDetailsService {

    /**
     * 根据用户名加载用户信息
     *
     * 注意：此方法在多租户场景下需要额外传入 tenantId
     * 这里通过 AppContextHolder 获取当前租户上下文
     */
    override fun loadUserByUsername(username: String): UserDetails {
        throw UsernameNotFoundException("请使用 loadUserByUsernameAndTenant 方法")
    }

    /**
     * 根据用户名和租户ID加载用户信息
     */
    fun loadUserByUsernameAndTenant(username: String, tenantId: Long): LoginUser {
        val user = userRepository.findByUsernameWithRoles(username, tenantId)
            ?: throw BizException(message = "用户不存在或已被禁用")

        if (user.status != UserStatus.ACTIVE) {
            throw BizException(message = "用户已被禁用")
        }

        // 收集所有权限码
        val permissions = mutableSetOf<String>()
        val roleDataScopes = mutableListOf<RoleDataScopeInfo>()
        var isSuperAdmin = false

        for (role in user.roles) {
            // 超级管理员角色
            if (role.code == "super_admin") {
                isSuperAdmin = true
            }

            // 收集菜单权限
            for (menu in role.menus) {
                menu.permission?.let { permissions.add(it) }
            }

            // 收集 API 权限
            for (permission in role.permissions) {
                permissions.add(permission.code)
            }

            // 收集数据权限配置
            roleDataScopes.add(
                RoleDataScopeInfo(
                    dataScope = role.dataScope,
                    specificOrgIds = role.specificOrgs.map { it.id }
                )
            )
        }

        return LoginUser(
            userId = user.id,
            tenantId = user.tenantId,
            orgId = user.org?.id,
            username = user.username,
            password = user.passwordHash,
            nickname = user.nickname,
            permissions = permissions,
            roleDataScopes = roleDataScopes,
            isSuperAdmin = isSuperAdmin,
            enabled = user.status == UserStatus.ACTIVE
        )
    }

    /**
     * 根据用户 ID 加载用户信息（用于 Token 刷新）
     */
    fun loadUserById(userId: Long): LoginUser {
        val user = userRepository.findByIdWithRoles(userId)
            ?: throw BizException(message = "用户不存在")

        return loadUserByUsernameAndTenant(user.username, user.tenantId)
    }
}

