package io.github.faustofanb.admin.module.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.faustofanb.admin.module.system.domain.entity.SysUser;
import io.github.faustofanb.admin.module.system.domain.entity.SysUserDraft;
import io.github.faustofanb.admin.module.system.model.LoginUser;
import io.github.faustofanb.admin.rpc.api.model.SysUserDTO;
import io.github.faustofanb.admin.rpc.api.model.UserInfoDTO;
import io.github.faustofanb.admin.rpc.api.system.RemoteUserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final RemoteUserService remoteUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfoDTO userInfo = remoteUserService.getUserInfo(username);
        if (userInfo == null || userInfo.getSysUser() == null) {
            log.info("Login user: {} not found.", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        SysUserDTO userDto = userInfo.getSysUser();
        if (userDto.getStatus() != null && userDto.getStatus() == 1) { // 1 is disabled
            log.info("Login user: {} is disabled.", username);
            throw new UsernameNotFoundException("User is disabled: " + username);
        }

        return createLoginUser(userInfo);
    }

    public UserDetails createLoginUser(UserInfoDTO userInfo) {
        SysUserDTO userDto = userInfo.getSysUser();
        SysUser user = SysUserDraft.$.produce(draft -> {
            draft.setId(userDto.getId());
            draft.setUserName(userDto.getUserName());
            draft.setNickName(userDto.getNickName());
            draft.setPassword(userDto.getPassword());
            draft.setStatus(userDto.getStatus());
            draft.setDeptId(userDto.getDeptId());
        });
        return new LoginUser(user, userInfo.getPermissions());
    }
}
