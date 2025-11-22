package io.github.faustofanb.admin.rpc.api.system;

import io.github.faustofanb.admin.rpc.api.model.SysUserDTO;
import io.github.faustofanb.admin.rpc.api.model.UserInfoDTO;

public interface RemoteUserService {

    UserInfoDTO getUserInfo(String username);

    boolean registerUser(SysUserDTO user);
}
