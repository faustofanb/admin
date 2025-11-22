package io.github.faustofanb.admin.module.system.service;

import io.github.faustofanb.admin.module.system.domain.entity.SysUser;

public interface SysUserService {

    SysUser selectUserByUserName(String userName);

    boolean checkUserNameUnique(String userName);

    void resetPassword(Long userId, String password);

    void updateUserStatus(Long userId, int status);
}
