package io.github.faustofanb.admin.module.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.faustofanb.admin.module.system.domain.entity.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long deptId;

    private SysUser user;

    private Set<String> permissions;

    private String token;

    private Long loginTime;

    private Long expireTime;

    public LoginUser(SysUser user, Set<String> permissions) {
        this.user = user;
        this.userId = user.id();
        this.deptId = user.dept() != null ? user.dept().id() : null;
        this.permissions = permissions;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return user.password();
    }

    @Override
    public String getUsername() {
        return user.userName();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return user.status() == 0;
    }
}
