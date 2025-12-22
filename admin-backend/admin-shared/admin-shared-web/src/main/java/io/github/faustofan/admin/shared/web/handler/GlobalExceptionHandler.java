package io.github.faustofan.admin.shared.web.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
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
    public ApiResponse<?> handleParamMissingException(HttpMessageNotReadableException e) {
        String message = e.getMessage();

        // 1. 针对 Jimmer/Jackson 的 "property 'xxx' must be specified" 错误进行正则提取
        if (message != null && message.contains("must be specified")) {
            Pattern pattern = Pattern.compile("the property \"(.*?)\" must be specified");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String fieldName = matcher.group(1);
                return ApiResponse.fail(UserErrorCode.PARAM_MISSING, "请求参数缺失必填字段: " + fieldName);
            }
            return ApiResponse.fail(UserErrorCode.PARAM_MISSING, "请求参数缺失必填字段");
        }

        // 2. 针对类型转换错误（如 Long 字段传了字符串）
        if (message != null && message.contains("Cannot deserialize value of type")) {
            // 例子: Cannot deserialize value of type `java.lang.Long` from String
            // "faustofan": not a valid `java.lang.Long` value
            Pattern pattern = Pattern.compile("Cannot deserialize value of type `(.+?)` from (.+?) \"(.*?)\":");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String type = matcher.group(1);
                String _ = matcher.group(2);
                String actualValue = matcher.group(3);
                // 尝试提取字段名
                Pattern fieldPattern = Pattern.compile(
                        "at \\[Source.*?; line: \\d+, column: \\d+\\] \\(through reference chain: .+?\\[\"(\\w+)\"\\]");
                Matcher fieldMatcher = fieldPattern.matcher(message);
                String field = fieldMatcher.find() ? fieldMatcher.group(1) : null;
                String msg = "请求参数";
                if (field != null) {
                    msg += "【" + field + "】";
                }
                msg += "类型错误，期望类型为 " + type.substring(type.lastIndexOf('.') + 1) + "，实际传值为 '" + actualValue + "'";
                return ApiResponse.fail(UserErrorCode.PARAM_INVALID, msg);
            }
        }

        // 3. 其他类型的 JSON 错误
        return ApiResponse.fail(UserErrorCode.PARAM_INVALID, "请求体 JSON 格式错误，无法解析");
    }

    /**
     * 处理参数校验异常
     *
     * @param e 参数校验异常
     * @return 包含所有校验错误信息的失败响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage) // 提取 message="xxxx"
                .collect(Collectors.joining("; "));
        return ApiResponse.fail(UserErrorCode.PARAM_INVALID, errorMessage);
    }

    /**
     * 处理业务异常
     *
     * @param ex 业务异常
     * @return 业务异常对应的失败响应
     */
    @ExceptionHandler(BizException.class)
    public ApiResponse<?> handleBizException(BizException ex) {
        return ApiResponse.fail(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理用户异常
     *
     * @param ex 用户异常
     * @return 用户异常对应的失败响应
     */
    @ExceptionHandler(UserException.class)
    public ApiResponse<?> handleUserException(UserException ex) {
        return ApiResponse.fail(ex.getErrorCode(), ex.getMessage());
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
        return ApiResponse.fail(SystemErrorCode.SYSTEM_ERROR);
    }

}