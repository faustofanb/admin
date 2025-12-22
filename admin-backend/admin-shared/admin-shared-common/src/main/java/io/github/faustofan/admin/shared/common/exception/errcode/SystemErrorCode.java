package io.github.faustofan.admin.shared.common.exception.errcode;

import io.github.faustofan.admin.shared.common.exception.ErrorCode;

public enum SystemErrorCode implements ErrorCode {
    SYSTEM_ERROR("C0000", "系统内部异常"),
    DATABASE_ERROR("C0100", "数据库访问异常"),
    CACHE_ERROR("C0101", "缓存访问异常"),
    RPC_ERROR("C0200", "远程服务调用失败"),
    TIMEOUT("C0300", "系统处理超时"),
    CONTEXT_MISSING("C0400", "上下文信息缺失"),
    TENANT_ID_MISSING("C0401", "租户ID缺失"),
    DISTRIBUTED_ACQUIRE_LOCK_FAIL("C0500", "分布式锁获取失败"),
    INTERRUPT_EXCEPTION("C0501", "系统中断异常"),
    ;

    private final String code;
    private final String message;

    SystemErrorCode(String code, String message) {
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
