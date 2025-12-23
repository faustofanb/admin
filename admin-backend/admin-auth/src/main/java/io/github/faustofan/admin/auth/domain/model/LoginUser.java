package io.github.faustofan.admin.auth.domain.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 登录用户信息
 * 实现 Spring Security 的 UserDetails 接口
 *
 * 包含用户的基本信息、权限信息、租户信息等
 * 在登录成功后会被缓存到 Redis 中
 */
public class LoginUser implements UserDetails {

    private final long userId;
    private final Long tenantId;
    private final long orgId;
    private final String orgName;
    private final String username;
    private final String password;
    private final String nickname;
    private final Map<Long, String> roles;
    private final Set<String> permissions;
    private final Set<String> dataPolicy;
    private final boolean isSuperAdmin;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;

    public LoginUser(
            Long userId,
            Long tenantId,
            Long orgId,
            String orgName,
            String username,
            String password,
            String nickname,
            Map<Long, String> roles,
            Set<String> permissions,
            Set<String> dataPolicy,
            boolean isSuperAdmin,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked
    ) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.orgId = orgId;
        this.orgName = orgName;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles != null ? roles : new HashMap<>();
        this.permissions = permissions != null ? permissions : new HashSet<>();
        this.dataPolicy = dataPolicy != null ? dataPolicy : new HashSet<>();
        this.isSuperAdmin = isSuperAdmin;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
    }

    // 可选：重载一个只带必需参数的构造方法
    public LoginUser(
            Long userId,
            Long tenantId,
            Long orgId,
            String orgName,
            String username,
            String password,
            String nickname,
            Map<Long, String> roles,
            Set<String> permissions,
            Set<String> dataPolicy
    ) {
        this(userId, tenantId, orgId, orgName, username, password, nickname, roles, permissions, dataPolicy,
                false, true, true, true, true
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 检查用户是否拥有指定权限
     */
    public boolean hasPermission(String permission) {
        if (isSuperAdmin)
            return true;
        return permissions.contains(permission) || permissions.contains("*:*:*");
    }

    public long getUserId() {
        return userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public long getOrgId() {
        return orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getNickname() {
        return nickname;
    }

    public Map<Long, String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Set<String> getDataPolicy() {
        return dataPolicy;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
