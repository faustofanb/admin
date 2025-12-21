package io.github.faustofan.admin.shared.common.exception;

/**
 * 通用错误码定义
 * 建议放在 common 模块
 */
public enum CommonErrorCode implements ErrorCode {

    // ===== 用户错误 A =====
    PARAM_INVALID("A0100", "请求参数不合法"),
    PARAM_MISSING("A0101", "缺少必要参数"),
    UNAUTHORIZED("A0200", "未登录或登录已过期"),
    FORBIDDEN("A0201", "无权限访问"),

    // ===== 业务错误 B =====
    BIZ_CONFLICT("B0100", "业务状态冲突"),
    DATA_NOT_FOUND("B0101", "数据不存在"),
    OPERATION_NOT_ALLOWED("B0102", "当前状态不允许该操作"),

    // ===== 系统错误 C =====
    SYSTEM_ERROR("C0000", "系统内部异常"),
    DATABASE_ERROR("C0100", "数据库访问异常"),
    CACHE_ERROR("C0101", "缓存访问异常"),
    RPC_ERROR("C0200", "远程服务调用失败"),
    TIMEOUT("C0300", "系统处理超时"),
    CONTEXT_MISSING("C0400", "上下文信息缺失"),
    TENANT_ID_MISSING("C0401", "租户ID缺失");

    private final String code;
    private final String message;

    CommonErrorCode(String code, String message) {
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

