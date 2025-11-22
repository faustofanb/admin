package io.github.faustofanb.admin.infra.web;

import io.github.faustofanb.admin.common.exception.BizErrorCode;
import io.github.faustofanb.admin.common.exception.BizException;
import io.github.faustofanb.admin.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e) {
        log.warn("BizException: {}", e.getMessage());
        return Result.fail(e.getErrorCode().getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException: {}", e.getMessage());
        return handleError(e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        log.warn("BindException: {}", e.getMessage());
        return handleError(e.getBindingResult());
    }

    private Result<Void> handleError(BindingResult bindingResult) {
        String message = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        return Result.fail(BizErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("System Error", e);
        return Result.fail(BizErrorCode.INTERNAL_ERROR);
    }
}
