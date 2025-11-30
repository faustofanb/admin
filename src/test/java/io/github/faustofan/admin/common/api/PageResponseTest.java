package io.github.faustofan.admin.common.api;

import io.github.faustofan.admin.common.PageResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("PageResponse 单元测试")
class PageResponseTest {

    @Test
    @DisplayName("of 方法应正确创建分页响应对象")
    void ofShouldCreatePageResponseCorrectly() {
        List<String> records = List.of("A", "B");
        PageResponse<String> response = PageResponse.of(records, 100, 2, 50);
        Assertions.assertEquals(records, response.records());
        Assertions.assertEquals(100, response.total());
        Assertions.assertEquals(2, response.page());
        Assertions.assertEquals(50, response.size());
    }

    @Test
    @DisplayName("totalPages 应正确计算总页数")
    void totalPagesShouldCalculateCorrectly() {
        PageResponse<String> response = new PageResponse<>(List.of(), 101, 1, 10);
        Assertions.assertEquals(11, response.totalPages());
    }

    @Test
    @DisplayName("totalPages 总数为0时应返回0")
    void totalPagesShouldBeZeroWhenTotalIsZero() {
        PageResponse<String> response = new PageResponse<>(List.of(), 0, 1, 10);
        Assertions.assertEquals(0, response.totalPages());
    }

    @Test
    @DisplayName("totalPages 每页大小为1时应等于总数")
    void totalPagesShouldEqualTotalWhenSizeIsOne() {
        PageResponse<String> response = new PageResponse<>(List.of(), 5, 1, 1);
        Assertions.assertEquals(5, response.totalPages());
    }

    @Test
    @DisplayName("records 可以为空列表")
    void recordsCanBeEmptyList() {
        PageResponse<String> response = new PageResponse<>(List.of(), 10, 1, 10);
        Assertions.assertTrue(response.records().isEmpty());
    }
}