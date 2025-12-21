package io.github.faustofan.admin.shared.common.exception;

/**
 * 用户输入或权限相关异常
 */
public class UserException extends BaseException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}

