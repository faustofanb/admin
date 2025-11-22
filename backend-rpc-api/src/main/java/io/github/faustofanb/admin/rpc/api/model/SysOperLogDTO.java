package io.github.faustofanb.admin.rpc.api.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysOperLogDTO implements Serializable {
    private String title;
    private Integer businessType;
    private String method;
    private String requestMethod;
    private Integer operatorType;
    private String operName;
    private String deptName;
    private String operUrl;
    private String operIp;
    private String operLocation;
    private String operParam;
    private String jsonResult;
    private Integer status;
    private String errorMsg;
    private LocalDateTime operTime;
    private Long costTime;
}
