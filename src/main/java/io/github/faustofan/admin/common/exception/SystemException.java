package io.github.faustofan.admin.common.exception;

public class SystemException extends RuntimeException {
    /**
     * 构造方法，指定异常消息
     *
     * @param message 异常消息
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * 构造方法，指定异常消息和原因
     *
     * @param message 异常消息
     * @param cause   异常原因
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
