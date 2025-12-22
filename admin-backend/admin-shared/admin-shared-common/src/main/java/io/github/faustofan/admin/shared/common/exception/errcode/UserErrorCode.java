package io.github.faustofan.admin.shared.common.exception.errcode;

import io.github.faustofan.admin.shared.common.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
    PARAM_INVALID("A0100", "请求参数不合法"),
    PARAM_MISSING("A0101", "缺少必要参数"),
    UNAUTHORIZED("A0201", "未登录或登录已过期"),
    FORBIDDEN("A0202", "无权限访问"),
    PASSWORD_ERROR("A0203", "用户名或密码错误"),
    INVALID_REFRESH_TOKEN("A0204", "无效的刷新令牌"),
    USERNAME_ALREADY_EXISTS("A0205", "用户名已存在"),
    ;

    private final String code;
    private final String message;

    UserErrorCode(String code, String message) {
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
