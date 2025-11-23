package io.github.faustofan.admin.boot.common.error;

/**
 * 错误码枚举类，用于统一管理系统中的错误码。
 * <p>
 * 每个枚举值代表一种错误类型，并关联一个字符串类型的错误码。
 * 主要用于认证、权限、校验、业务冲突、限流和系统内部错误等场景。
 */
public enum ErrorCode {
    /**
     * 认证失败，Token无效
     */
    AUTH_INVALID_TOKEN("101"),
    /**
     * 未授权访问
     */
    AUTH_UNAUTHORIZED("102"),
    /**
     * 权限不足，禁止访问
     */
    PERM_FORBIDDEN("103"),
    /**
     * 参数校验失败
     */
    VALIDATION_FAILED("104"),
    /**
     * 业务冲突
     */
    BIZ_CONFLICT("201"),
    /**
     * 请求被限流
     */
    RATE_LIMITED("301"),
    /**
     * 系统内部错误
     */
    SYS_INTERNAL_ERROR("500");

    /**
     * 错误码字符串
     */
    private final String code;

    /**
     * 构造方法
     *
     * @param code 错误码字符串
     */
    ErrorCode(String code) {
        this.code = code;
    }

    /**
     * 获取错误码字符串
     *
     * @return 错误码
     */
    public String code() {
        return code;
    }
}

