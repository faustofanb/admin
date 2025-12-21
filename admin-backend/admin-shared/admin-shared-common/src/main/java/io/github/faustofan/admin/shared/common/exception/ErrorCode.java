package io.github.faustofan.admin.shared.common.exception;

/**
 * 错误码顶层接口
 * 所有错误码必须实现该接口
 */
public interface ErrorCode {

    /**
     * 错误码（唯一且稳定）
     */
    String getCode();

    /**
     * 面向用户或调用方的错误描述
     */
    String getMessage();
}
