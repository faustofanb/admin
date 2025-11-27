package io.github.faustofan.admin.common.error;

/**
 * 错误类别枚举类，用于标识错误码所属的类别。
 * <p>
 * 包含认证、权限、校验、业务、限流、系统、集成等错误类型。
 */
public enum ErrorCategory {
    /**
     * 认证相关错误
     */
    AUTH("AUTH"),
    /**
     * 权限相关错误
     */
    PERMISSION("PERMISSION"),
    /**
     * 参数校验相关错误
     */
    VALIDATION("VALIDATION"),
    /**
     * 业务相关错误
     */
    BUSINESS("BUSINESS"),
    /**
     * 限流相关错误
     */
    RATE_LIMIT("RATE_LIMIT"),
    /**
     * 系统相关错误
     */
    SYSTEM("SYSTEM"),
    /**
     * 集成相关错误
     */
    INTEGRATION("INTEGRATION")
    ;

    /**
     * 错误类别前缀
     */
    private String prefix;

    /**
     * 构造方法
     *
     * @param prefix 错误类别前缀
     */
    ErrorCategory(String prefix) {
        this.prefix = prefix;
    }

}