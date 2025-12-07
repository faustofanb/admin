package io.github.faustofan.admin.auth.domain.model

import io.github.faustofan.admin.shared.application.context.DataScope
import io.github.faustofan.admin.shared.application.context.RoleDataScopeInfo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * 登录用户信息
 * 实现 Spring Security 的 UserDetails 接口
 *
 * 包含用户的基本信息、权限信息、租户信息等
 * 在登录成功后会被缓存到 Redis 中
 */
data class LoginUser(
    val userId: Long,
    val tenantId: Long,
    val orgId: Long?,
    private val username: String,
    private val password: String,
    val nickname: String?,
    val permissions: Set<String>,
    val roleDataScopes: List<RoleDataScopeInfo>,
    val isSuperAdmin: Boolean = false,
    private val enabled: Boolean = true,
    private val accountNonExpired: Boolean = true,
    private val credentialsNonExpired: Boolean = true,
    private val accountNonLocked: Boolean = true
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return permissions.map { SimpleGrantedAuthority(it) }
    }

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = accountNonExpired

    override fun isAccountNonLocked(): Boolean = accountNonLocked

    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired

    override fun isEnabled(): Boolean = enabled

    /**
     * 检查用户是否拥有指定权限
     */
    fun hasPermission(permission: String): Boolean {
        if (isSuperAdmin) return true
        return permissions.contains(permission) || permissions.contains("*:*:*")
    }

    /**
     * 获取用户的最高数据权限范围
     * 优先级: ALL > SAME_DEPT_TREE > SAME_DEPT > CUSTOM > SELF
     */
    fun getEffectiveDataScope(): DataScope {
        if (isSuperAdmin) return DataScope.ALL

        val priority = mapOf(
            DataScope.ALL to 5,
            DataScope.SAME_DEPT_TREE to 4,
            DataScope.SAME_DEPT to 3,
            DataScope.CUSTOM to 2,
            DataScope.SELF to 1
        )

        return roleDataScopes
            .maxByOrNull { priority[it.dataScope] ?: 0 }
            ?.dataScope ?: DataScope.SELF
    }

    /**
     * 获取自定义部门 ID 列表（当 DataScope 为 CUSTOM 时使用）
     */
    fun getCustomOrgIds(): Set<Long> {
        return roleDataScopes
            .filter { it.dataScope == DataScope.CUSTOM }
            .flatMap { it.specificOrgIds }
            .toSet()
    }
}

