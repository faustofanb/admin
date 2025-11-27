package io.github.faustofan.admin.common.error;

import java.util.Optional;

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
    AUTH_INVALID_TOKEN("100", ErrorCategory.AUTH),
    /**
     * 认证失败，Token过期
     */
    AUTH_TOKEN_EXPIRED("101", ErrorCategory.AUTH),
    /**
     * 认证失败，凭证错误
     */
    AUTH_BAD_CREDENTIALS("102", ErrorCategory.AUTH),
    /**
     * 认证失败，账号被禁用
     */
    AUTH_ACCOUNT_DISABLED("103", ErrorCategory.AUTH),
    /**
     * 认证失败，账号被锁定
     */
    AUTH_ACCOUNT_LOCKED("104", ErrorCategory.AUTH),
    /**
     * 认证失败，账号过期
     */
    AUTH_ACCOUNT_EXPIRED("105", ErrorCategory.AUTH),
    /**
     * 认证失败，凭证过期
     */
    AUTH_CREDENTIALS_EXPIRED("106", ErrorCategory.AUTH),
    /**
     * 认证失败，未授权
     */
    AUTH_UNAUTHORIZED("107", ErrorCategory.AUTH),
    /**
     * 权限不足，禁止访问
     */
    PERM_FORBIDDEN("200", ErrorCategory.PERMISSION),
    /**
     * 参数校验失败
     */
    VALIDATION_FAILED("300", ErrorCategory.VALIDATION),
    /**
     * 业务冲突
     */
    BIZ_CONFLICT("400", ErrorCategory.BUSINESS),
    /**
     * 业务状态非法
     */
    BIZ_ILLEGAL_STATE("401", ErrorCategory.BUSINESS),
    /**
     * 业务数据未找到
     */
    BIZ_NOT_FOUND("404", ErrorCategory.BUSINESS),
    /**
     * 系统内部错误
     */
    SYS_INTERNAL_ERROR("500", ErrorCategory.SYSTEM),
    /**
     * 系统依赖错误
     */
    SYS_DEPENDENCY_ERROR("501", ErrorCategory.SYSTEM),
    /**
     * 系统意外错误
     */
    SYS_UNEXPECTED_ERROR("502", ErrorCategory.SYSTEM),
    /**
     * 请求被限流
     */
    RATE_LIMITED("600", ErrorCategory.RATE_LIMIT),
    /**
     * 集成错误
     */
    INTEGRATION_ERROR("700", ErrorCategory.INTEGRATION),
    /**
     * 集成超时
     */
    INTEGRATION_TIMEOUT("701", ErrorCategory.INTEGRATION),
    /**
     * 集成服务不可用
     */
    INTEGRATION_UNAVAILABLE("702", ErrorCategory.INTEGRATION)
    ;

    /**
     * 错误码字符串
     */
    private final String code;

    /**
     * 错误类别
     */
    private final ErrorCategory errorCategory;

    /**
     * 构造方法
     *
     * @param code 错误码字符串
     * @param errorCategory 错误类别
     */
    ErrorCode(String code, ErrorCategory errorCategory) {
        this.code = code;
        this.errorCategory = errorCategory;
    }

    /**
     * 根据错误码字符串查找对应的枚举值
     *
     * @param code 错误码字符串
     * @return 匹配的 ErrorCode 枚举值，若未找到则返回 Optional.empty()
     */
    public static Optional<ErrorCode> fromCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code().equals(code)) {
                return Optional.of(errorCode);
            }
        }
        return Optional.empty();
    }

    /**
     * 获取错误码字符串
     *
     * @return 错误码
     */
    public String code() {
        return code;
    }

    /**
     * 获取错误类别
     *
     * @return 错误类别
     */
    public ErrorCategory errorCategory() {
        return errorCategory;
    }
}