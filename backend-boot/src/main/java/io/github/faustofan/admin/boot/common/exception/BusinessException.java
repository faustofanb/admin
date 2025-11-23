package io.github.faustofan.admin.boot.common.exception;

import io.github.faustofan.admin.boot.common.error.ErrorCode;
import lombok.Getter;

/**
 * 业务异常类，用于在业务处理过程中抛出自定义异常。
 * <p>
 * 该异常包含错误码和详细信息，便于异常追踪和处理。
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 业务错误码
     */
    private final ErrorCode errorCode;
    /**
     * 异常详细信息
     */
    private final String detail;

    /**
     * 构造方法，指定错误码和详细信息
     *
     * @param errorCode 错误码
     * @param detail    异常详细信息
     */
    public BusinessException(ErrorCode errorCode, String detail) {
        super(errorCode.name() + ": " + detail);
        this.errorCode = errorCode;
        this.detail = detail;
    }

    /**
     * 构造方法，仅指定错误码
     *
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        this(errorCode, "");
    }

    /**
     * 构造方法，指定错误码和异常原因
     *
     * @param errorCode 错误码
     * @param cause     异常原因
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.name() + ": " + cause.getMessage(), cause);
        this.errorCode = errorCode;
        this.detail = cause.getMessage();
    }

}
