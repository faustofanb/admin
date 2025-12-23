package io.github.faustofan.admin.shared.web.handler;

import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.faustofan.admin.shared.common.dto.ApiResponse;
import io.github.faustofan.admin.shared.common.exception.BizException;
import io.github.faustofan.admin.shared.common.exception.SystemException;
import io.github.faustofan.admin.shared.common.exception.UserException;
import io.github.faustofan.admin.shared.common.exception.errcode.SystemErrorCode;
import io.github.faustofan.admin.shared.common.exception.errcode.UserErrorCode;

/**
 * 全局异常处理器
 * <p>
 * 统一处理控制器层抛出的异常，返回标准的 API 响应格式，便于前端统一处理错误。
 * 支持参数校验异常、业务异常、用户异常、系统异常及未知异常。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 日志记录器 */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理请求体不可读异常（通常是 JSON 解析错误）
     *
     * @param e 请求体不可读异常
     * @return 包含具体错误信息的失败响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        // 1. 处理 Jackson 映射异常 (字段类型不匹配、JSON 结构错误)
        if (cause instanceof JsonMappingException mappingException) {
            // 获取产生错误的字段路径 (例如: "query.userId" 或 "users[0].age")
            String fieldName = mappingException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.joining("."));

            // 情况 A: 类型转换错误 (例如 String 传给了 Long)
            if (cause instanceof InvalidFormatException formatException) {
                String targetType = formatException.getTargetType().getSimpleName();
                Object actualValue = formatException.getValue();
                String msg = String.format("参数【%s】类型错误：期望类型为 %s，实际输入为 '%s'",
                        fieldName, targetType, actualValue);
                return ApiResponse.fail(UserErrorCode.PARAM_INVALID, msg);
            }

            // 情况 B: 必填字段缺失 (针对 Jimmer 或 Jackson 的注解)
            // Jimmer 的 "must be specified" 往往封装在 MismatchedInputException 中
            if (cause instanceof MismatchedInputException mismatchedException) {
                String originalMessage = mismatchedException.getOriginalMessage();
                if (originalMessage != null && originalMessage.contains("must be specified")) {
                    return ApiResponse.fail(UserErrorCode.PARAM_MISSING,
                            "缺失必填字段: " + (fieldName.isEmpty() ? "未知字段" : fieldName));
                }
            }

            // 其他映射错误
            if (!fieldName.isEmpty()) {
                return ApiResponse.fail(UserErrorCode.PARAM_INVALID, "参数【" + fieldName + "】格式错误");
            }
        }

        // 2. 特殊处理：如果还是没拿到信息，但包含 Jimmer 常见的缺失提示 (兜底)
        String rawMsg = e.getMessage();
        if (rawMsg != null && rawMsg.contains("must be specified")) {
            // 如果无法通过 Jackson 路径获取，再尝试简单的正则提取字段名
            return ApiResponse.fail(UserErrorCode.PARAM_MISSING, "请求参数缺失必填字段");
        }

        // 3. 最后的兜底：JSON 语法错误（如少了个逗号、括号不匹配）
        logger.error("JSON 解析异常: ", e);
        return ApiResponse.fail(UserErrorCode.PARAM_INVALID, "请求体 JSON 格式错误，无法解析");
    }

    /**
     * 统一处理参数校验异常
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class
    })
    public ApiResponse<?> handleBindingException(Exception e) {
        String message = "参数校验失败";

        if (e instanceof MethodArgumentNotValidException ex) {
            message = ex.getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .findFirst()
                    .orElse(ex.getMessage());
        } else if (e instanceof BindException ex) {
            message = ex.getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .findFirst()
                    .orElse(ex.getMessage());
        } else if (e instanceof ConstraintViolationException ex) {
            message = ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .orElse(ex.getMessage());
        }

        logger.warn("参数校验异常: {}", message);
        return ApiResponse.fail(UserErrorCode.PARAM_INVALID, message);
    }

    /**
     * 处理业务异常
     *
     * @param ex 业务异常
     * @return 业务异常对应的失败响应
     */
    @ExceptionHandler(BizException.class)
    public ApiResponse<?> handleBizException(BizException ex) {
        logger.warn(ex.getMessage());
        return ApiResponse.fail(ex.getErrorCode());
    }

    /**
     * 处理用户异常
     *
     * @param ex 用户异常
     * @return 用户异常对应的失败响应
     */
    @ExceptionHandler(UserException.class)
    public ApiResponse<?> handleUserException(UserException ex) {
        logger.warn(ex.getMessage());
        return ApiResponse.fail(ex.getErrorCode());
    }

    /**
     * 处理系统异常
     *
     * @param ex 系统异常
     * @return 系统异常对应的失败响应，并记录错误日志
     */
    @ExceptionHandler(SystemException.class)
    public ApiResponse<?> handleSystemException(SystemException ex) {
        logger.error("System error", ex);
        ex.printStackTrace();
        return ApiResponse.fail(ex.getErrorCode());
    }

    /**
     * 处理未知异常
     *
     * @param ex 未知异常
     * @return 通用系统错误的失败响应，并记录错误日志
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleUnknownException(Exception ex) {
        logger.error("Unknown error", ex);
        ex.printStackTrace();
        return ApiResponse.fail(SystemErrorCode.SYSTEM_ERROR);
    }

}