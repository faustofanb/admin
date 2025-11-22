package io.github.faustofanb.admin.module.system.model;

import lombok.Data;

@Data
public class LoginBody {
    private String username;
    private String password;
    private String code;
    private String uuid;
}
