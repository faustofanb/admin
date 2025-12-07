package io.github.faustofan.admin.auth.domain.service

import io.github.faustofan.admin.auth.domain.model.LoginUser
import io.github.faustofan.admin.shared.application.context.RoleDataScopeInfo
import io.github.faustofan.admin.shared.infrastructure.exception.BizException
import io.github.faustofan.admin.system.domain.model.UserStatus
import io.github.faustofan.admin.system.infrastructure.persistence.SysUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * 用户详情服务
 *
 * 实现 Spring Security 的 UserDetailsService 接口
 * 用于登录时加载用户信息
 *
 * 注意：缓存由 LoginUserCacheService 统一处理，本类只负责业务逻辑
 */
@Service
class UserDetailsServiceImpl(
    private val userRepository: SysUserRepository,
    private val loginUserCacheService: LoginUserCacheService
) : UserDetailsService {

    /**
     * 根据用户名加载用户信息
     * Spring Security 标准接口，多租户场景下请使用 loadUserByUsernameAndTenant
     */
    override fun loadUserByUsername(username: String): UserDetails {
        throw UsernameNotFoundException("请使用 loadUserByUsernameAndTenant 方法")
    }

    /**
     * 根据用户名和租户ID加载用户信息
     */
    fun loadUserByUsernameAndTenant(username: String, tenantId: Long): LoginUser {
        return loginUserCacheService.getByUsernameAndTenant(username, tenantId) {
            loadUserFromDatabase(username, tenantId)
        } ?: throw BizException(message = "用户不存在或已被禁用")
    }

    /**
     * 根据用户 ID 加载用户信息（用于 Token 刷新）
     */
    fun loadUserById(userId: Long): LoginUser {
        return loginUserCacheService.getByUserId(userId) {
            val user = userRepository.findByIdWithRoles(userId)
                ?: throw BizException(message = "用户不存在")
            loadUserFromDatabase(user.username, user.tenantId)
        } ?: throw BizException(message = "用户不存在")
    }

    /**
     * 从数据库加载用户信息
     */
    private fun loadUserFromDatabase(username: String, tenantId: Long): LoginUser? {
        val user = userRepository.findByUsernameWithRoles(username, tenantId)
            ?: return null

        if (user.status != UserStatus.ACTIVE) {
            throw BizException(message = "用户已被禁用")
        }

        val permissions = mutableSetOf<String>()
        val roleDataScopes = mutableListOf<RoleDataScopeInfo>()
        var isSuperAdmin = false

        for (role in user.roles) {
            if (role.code == "super_admin") {
                isSuperAdmin = true
            }

            for (menu in role.menus) {
                menu.permission?.let { permissions.add(it) }
            }

            for (permission in role.permissions) {
                permissions.add(permission.code)
            }

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
}

