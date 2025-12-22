package io.github.faustofan.admin.shared.common.exception;

/**
 * 业务规则异常
 * 不需要打印 ERROR 级别日志
 */
public class BizException extends BaseException {

    public BizException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BizException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }

    public BizException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
