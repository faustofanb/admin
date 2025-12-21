package io.github.faustofan.admin.shared.common.exception;

/**
 * 业务错误码定义
 */
public enum BizErrorCode implements ErrorCode {
    BIND_TENANT_NOT_EXIST("B0103", "用户绑定的租户不存在"),
    BIND_ORG_NOT_EXIST("B0104", "用户绑定的组织不存在"),
    BIND_ROLE_NOT_EXIST("B0105", "用户绑定的角色不存在")
    ;

    private final String code;
    private final String message;

    BizErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
