package io.github.faustofan.admin.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务异常类，用于在业务处理过程中抛出自定义异常。
 * <p>
 * 该异常包含错误码和详细信息，便于异常追踪和处理。
 */
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
     * 自定义属性
     */
    private Map<String, Object> attributes;

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

    /**
     * 添加自定义属性
     *
     * @param key   属性键
     * @param value 属性值
     * @return 当前异常对象
     */
    public BusinessException withAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
        return this;
    }

    /**
     * 获取所有自定义属性
     *
     * @return 自定义属性映射
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 获取业务错误码
     *
     * @return 业务错误码
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 获取异常详细信息
     *
     * @return 异常详细信息
     */
    public String getDetail() {
        return detail;
    }
}
