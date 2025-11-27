package io.github.faustofan.admin.common.exception;

import org.springframework.validation.FieldError;

import java.util.List;

/**
 * 参数校验异常类，用于封装字段校验失败的信息。
 * <p>
 * 继承自 RuntimeException，包含所有校验失败字段的错误信息列表。
 */
public class ValidationException extends RuntimeException {

    /**
     * 校验失败字段的错误信息列表
     */
    private final List<String> fieldErrors;

    /**
     * 构造方法，根据字段错误信息列表创建异常
     *
     * @param fieldErrors 字段错误信息列表
     */
    public ValidationException(List<String> fieldErrors) {
        super("Validation failed for fields: " + String.join(", ", fieldErrors));
        this.fieldErrors = fieldErrors;
    }

    /**
     * 根据 Spring 的 FieldError 列表创建 ValidationException
     *
     * @param fieldErrors FieldError 列表
     * @return 封装了所有字段错误信息的 ValidationException
     */
    public static ValidationException of(List<FieldError> fieldErrors) {
        List<String> errorMessages = fieldErrors.stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return new ValidationException(errorMessages);

    }

    /**
     * 获取所有字段的错误信息列表
     *
     * @return 字段错误信息列表
     */
    public List<String> getFieldErrors() {
        return fieldErrors;
    }
}