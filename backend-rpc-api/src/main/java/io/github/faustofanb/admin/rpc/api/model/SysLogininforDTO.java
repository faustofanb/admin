package io.github.faustofanb.admin.rpc.api.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysLogininforDTO implements Serializable {
    private String userName;
    private String ipaddr;
    private String loginLocation;
    private String browser;
    private String os;
    private String status;
    private String msg;
    private LocalDateTime loginTime;
}
