package io.github.faustofanb.admin.module.system.service.impl;

import io.github.faustofanb.admin.module.system.domain.entity.SysUser;
import io.github.faustofanb.admin.module.system.model.LoginUser;
import io.github.faustofanb.admin.module.system.service.SysMenuService;
import io.github.faustofanb.admin.module.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final SysUserService userService;
    private final SysMenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if (user == null) {
            log.info("Login user: {} not found.", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        if (user.status() == 1) { // 1 is disabled
            log.info("Login user: {} is disabled.", username);
            throw new UsernameNotFoundException("User is disabled: " + username);
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        Set<String> perms = menuService.selectPermsByUserId(user.id());
        return new LoginUser(user, perms);
    }
}
