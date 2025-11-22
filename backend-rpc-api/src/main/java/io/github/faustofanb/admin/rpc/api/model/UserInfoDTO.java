package io.github.faustofanb.admin.rpc.api.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Set;

@Data
public class UserInfoDTO implements Serializable {
    private SysUserDTO sysUser;
    private Set<String> permissions;
    private Set<String> roles;
}
