package io.github.faustofanb.admin.rpc.api.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SysUserDTO implements Serializable {
    private Long id;
    private String userName;
    private String nickName;
    private String email;
    private String phonenumber;
    private String sex;
    private String avatar;
    private String password;
    private Integer status;
    private String loginIp;
    private LocalDateTime loginDate;
    private String remark;
    private Long deptId;
    private Set<String> roleKeys;
}
