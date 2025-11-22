package io.github.faustofanb.admin.rpc.impl.system;

import io.github.faustofanb.admin.module.system.domain.entity.SysUser;
import io.github.faustofanb.admin.module.system.service.SysMenuService;
import io.github.faustofanb.admin.module.system.service.SysUserService;
import io.github.faustofanb.admin.rpc.api.model.SysUserDTO;
import io.github.faustofanb.admin.rpc.api.model.UserInfoDTO;
import io.github.faustofanb.admin.rpc.api.system.RemoteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RemoteUserServiceImpl implements RemoteUserService {

    private final SysUserService userService;
    private final SysMenuService menuService;

    @Override
    public UserInfoDTO getUserInfo(String username) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (sysUser == null) {
            return null;
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        SysUserDTO sysUserDTO = new SysUserDTO();

        // Manual mapping or BeanUtils (Jimmer entities are interfaces, but getters
        // work)
        sysUserDTO.setId(sysUser.id());
        sysUserDTO.setUserName(sysUser.userName());
        sysUserDTO.setNickName(sysUser.nickName());
        sysUserDTO.setEmail(sysUser.email());
        sysUserDTO.setPhonenumber(sysUser.phonenumber());
        sysUserDTO.setSex(sysUser.sex());
        sysUserDTO.setAvatar(sysUser.avatar());
        sysUserDTO.setPassword(sysUser.password());
        sysUserDTO.setStatus(sysUser.status());
        sysUserDTO.setLoginIp(sysUser.loginIp());
        sysUserDTO.setLoginDate(sysUser.loginDate());
        sysUserDTO.setRemark(sysUser.remark());
        if (sysUser.dept() != null) {
            sysUserDTO.setDeptId(sysUser.dept().id());
        }

        userInfoDTO.setSysUser(sysUserDTO);

        Set<String> perms = menuService.selectPermsByUserId(sysUser.id());
        userInfoDTO.setPermissions(perms);

        // Roles are not yet fully implemented in SysUserService/SysMenuService to
        // return Set<String> roleKeys
        // For now, leave roles empty or implement selectRoleKeysByUserId

        return userInfoDTO;
    }

    @Override
    public boolean registerUser(SysUserDTO user) {
        // TODO: Implement registration logic in SysUserService and call it here
        return false;
    }
}
