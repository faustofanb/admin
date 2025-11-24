package io.github.faustofan.admin.boot.common.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("PageRequest 单元测试")
class PageRequestTest {

    @Test
    @DisplayName("of(page, size) 应创建无排序规则的分页请求")
    void ofShouldCreatePageRequestWithoutSort() {
        PageRequest request = PageRequest.of(2, 10);
        Assertions.assertEquals(2, request.page());
        Assertions.assertEquals(10, request.size());
        Assertions.assertTrue(request.sort().isEmpty());
    }

    @Test
    @DisplayName("of(page, size) 页码或每页大小为0或负数时应抛出异常")
    void ofShouldThrowExceptionForNonPositivePageOrSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> PageRequest.of(0, 10));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PageRequest.of(1, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PageRequest.of(-1, 10));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PageRequest.of(1, -5));
    }

    @Test
    @DisplayName("offset() 应正确计算偏移量")
    void offsetShouldCalculateCorrectOffset() {
        PageRequest request = new PageRequest(3, 20, List.of());
        Assertions.assertEquals(40, request.offset());
    }

    @Test
    @DisplayName("offset() 页码为1时偏移量应为0")
    void offsetShouldBeZeroForFirstPage() {
        PageRequest request = new PageRequest(1, 15, List.of());
        Assertions.assertEquals(0, request.offset());
    }

    @Test
    @DisplayName("sort 列表可正常传入并保存")
    void sortListShouldBeStoredCorrectly() {
        SortOrder order = SortOrder.asc("name");
        PageRequest request = new PageRequest(1, 10, List.of(order));
        Assertions.assertEquals(1, request.sort().size());
        Assertions.assertEquals(order, request.sort().get(0));
    }
}