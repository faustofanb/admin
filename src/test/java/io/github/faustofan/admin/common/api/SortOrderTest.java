package io.github.faustofan.admin.common.api;

import io.github.faustofan.admin.common.SortOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SortOrder 单元测试")
class SortOrderTest {

    @Test
    @DisplayName("asc 方法应创建升序排序规则")
    void ascShouldCreateAscendingSortOrder() {
        SortOrder order = SortOrder.asc("name");
        Assertions.assertEquals("name", order.property());
        Assertions.assertEquals(SortOrder.Direction.ASC, order.direction());
    }

    @Test
    @DisplayName("desc 方法应创建降序排序规则")
    void descShouldCreateDescendingSortOrder() {
        SortOrder order = SortOrder.desc("age");
        Assertions.assertEquals("age", order.property());
        Assertions.assertEquals(SortOrder.Direction.DESC, order.direction());
    }

    @Test
    @DisplayName("Direction 枚举应包含 ASC 和 DESC")
    void directionEnumShouldContainAscAndDesc() {
        Assertions.assertNotNull(SortOrder.Direction.valueOf("ASC"));
        Assertions.assertNotNull(SortOrder.Direction.valueOf("DESC"));
    }

    @Test
    @DisplayName("SortOrder 属性和方向应正确保存")
    void sortOrderShouldStorePropertyAndDirection() {
        SortOrder order = new SortOrder("score", SortOrder.Direction.DESC);
        Assertions.assertEquals("score", order.property());
        Assertions.assertEquals(SortOrder.Direction.DESC, order.direction());
    }
}