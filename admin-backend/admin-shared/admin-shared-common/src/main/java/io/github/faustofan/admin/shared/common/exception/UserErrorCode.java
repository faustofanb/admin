package io.github.faustofan.admin.shared.common.exception;

public enum UserErrorCode implements ErrorCode {

    USER_NOT_EXIST("B1001", "用户不存在"),
    USER_DISABLED("B1002", "用户已被禁用"),
    USER_NOT_EXIST_OR_DISABLED("B1003", "用户不存在或已被禁用"),
    PASSWORD_ERROR("A1001", "用户名或密码错误"),
    INVALID_REFRESH_TOKEN("A1002", "无效的刷新令牌"),
    USERNAME_ALREADY_EXISTS("B1004", "用户名已存在"),
    PWD_CHANGE_UNSUPPORTED("A1005", "当前状态不支持修改密码")
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
