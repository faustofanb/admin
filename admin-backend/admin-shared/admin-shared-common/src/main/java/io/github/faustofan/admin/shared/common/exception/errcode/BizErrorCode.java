package io.github.faustofan.admin.shared.common.exception.errcode;

import io.github.faustofan.admin.shared.common.exception.ErrorCode;

/**
 * 业务错误码定义
 */
public enum BizErrorCode implements ErrorCode {
    BIZ_CONFLICT("B0100", "业务状态冲突"),
    DATA_NOT_FOUND("B0101", "数据不存在"),
    OPERATION_NOT_ALLOWED("B0102", "当前状态不允许该操作"),
    PWD_CHANGE_UNSUPPORTED("B0103", "当前状态不支持修改密码"),
    USER_NOT_EXIST_OR_DISABLED("B0201", "用户不存在或已被禁用"),
    USER_NOT_EXIST("B0202", "用户不存在"),
    USER_DISABLED("B0203", "用户已被禁用"),
    BIND_TENANT_NOT_EXIST("B0204", "用户绑定的租户不存在"),
    BIND_ORG_NOT_EXIST("B0205", "用户绑定的组织不存在"),
    BIND_ROLE_NOT_EXIST("B0206", "用户绑定的角色不存在");

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
