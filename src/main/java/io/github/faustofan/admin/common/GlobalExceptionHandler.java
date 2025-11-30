package io.github.faustofan.admin.common;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常处理器，统一处理系统中的异常并返回标准响应格式。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常，返回失败响应。
     *
     * @param ex      业务异常对象
     * @param request 当前 Web 请求
     * @return 标准 API 响应，包含错误码、错误详情和 traceId
     */
    @ExceptionHandler(value = BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException ex, WebRequest request) {
        String traceId = request.getHeader("TRACE_ID");
        return ApiResponse.failure(
                ex.getErrorCode().code(),
                ex.getDetail()
        ).withTraceId(traceId);
    }

    /**
     * 处理参数校验异常，返回校验失败的详细信息。
     *
     * @param ex      参数校验异常对象
     * @param request 当前 Web 请求
     * @return 标准 API 响应，包含校验失败错误码、错误信息和 traceId
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        String traceId = request.getHeader("TRACE_ID");
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation failed");
        return ApiResponse.failure(
                ErrorCode.VALIDATION_FAILED.code(), // VALIDATION_FAILED
                errorMessage
        ).withTraceId(traceId);
    }

    /**
     * 处理未捕获的异常，返回系统内部错误信息。
     *
     * @param ex      未捕获的异常对象
     * @param request 当前 Web 请求
     * @return 标准 API 响应，包含系统内部错误码、异常信息和 traceId
     */
    @ExceptionHandler(value = Throwable.class)
    public ApiResponse<?> handleUnhandledException(
            Throwable ex,
            WebRequest request
    ) {
        String traceId = request.getHeader("TRACE_ID");
        return ApiResponse.failure(
                ErrorCode.SYS_INTERNAL_ERROR.code(), // SYS_INTERNAL_ERROR
                ex.getMessage()
        ).withTraceId(traceId);
    }
}
