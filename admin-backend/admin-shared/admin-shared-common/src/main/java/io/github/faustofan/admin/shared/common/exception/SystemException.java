package io.github.faustofan.admin.shared.common.exception;

/**
 * 系统异常
 */
public class SystemException extends BaseException {

    public SystemException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SystemException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }

    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
