package io.github.faustofanb.admin.common.model;

import io.github.faustofanb.admin.common.exception.BizErrorCode;
import io.github.faustofanb.admin.common.exception.ErrorCode;
import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;
    private String traceId;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(BizErrorCode.SUCCESS.getCode());
        result.setMessage(BizErrorCode.SUCCESS.getMessage());
        result.setData(data);
        result.setTraceId(MDC.get("traceId"));
        return result;
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return fail(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> Result<T> fail(String message) {
        return fail(BizErrorCode.INTERNAL_ERROR.getCode(), message);
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setTraceId(MDC.get("traceId"));
        return result;
    }
}
