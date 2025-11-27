package io.github.faustofan.admin.common.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("ApiResponse 单元测试")
class ApiResponseTest {

    @Test
    @DisplayName("success(data) 应返回包含数据的成功响应")
    void successWithDataShouldReturnSuccessResponse() {
        String expectedData = "hello";
        ApiResponse<String> response = ApiResponse.success(expectedData);
        Assertions.assertEquals(String.valueOf(HttpStatus.OK.value()), response.code());
        Assertions.assertEquals("Success", response.message());
        Assertions.assertEquals("", response.traceId());
        Assertions.assertEquals(expectedData, response.data());
    }

    @Test
    @DisplayName("success() 应返回无数据的成功响应")
    void successWithoutDataShouldReturnSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success();
        Assertions.assertEquals(String.valueOf(HttpStatus.OK.value()), response.code());
        Assertions.assertEquals("Success", response.message());
        Assertions.assertEquals("", response.traceId());
        Assertions.assertNull(response.data());
    }

    @Test
    @DisplayName("failure(code, message) 应返回失败响应")
    void failureShouldReturnFailureResponse() {
        String errorCode = "400";
        String errorMsg = "Bad Request";
        ApiResponse<String> response = ApiResponse.failure(errorCode, errorMsg);
        Assertions.assertEquals(errorCode, response.code());
        Assertions.assertEquals(errorMsg, response.message());
        Assertions.assertEquals("", response.traceId());
        Assertions.assertNull(response.data());
    }

    @Test
    @DisplayName("withTraceId(traceId) 应返回带 traceId 的响应")
    void withTraceIdShouldReturnResponseWithTraceId() {
        ApiResponse<String> response = ApiResponse.success("data");
        String traceId = "abc123";
        ApiResponse<String> traced = response.withTraceId(traceId);
        Assertions.assertEquals(traceId, traced.traceId());
        Assertions.assertEquals(response.code(), traced.code());
        Assertions.assertEquals(response.message(), traced.message());
        Assertions.assertEquals(response.data(), traced.data());
    }

    @Test
    @DisplayName("withTraceId(null) 应支持 traceId 为 null")
    void withTraceIdNullShouldReturnResponseWithNullTraceId() {
        ApiResponse<String> response = ApiResponse.success("data");
        ApiResponse<String> traced = response.withTraceId(null);
        Assertions.assertNull(traced.traceId());
    }
}
