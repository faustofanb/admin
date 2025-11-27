package io.github.faustofan.admin.common.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AppContext 单元测试")
class AppContextTest {

    @Test
    @DisplayName("构造器应正确设置所有字段")
    void constructorShouldSetAllFields() {
        String tenantId = "tenant1";
        String userId = "user1";
        String requestId = "req123";
        String traceId = "trace456";
        AppContext context = new AppContext(tenantId, userId, requestId, traceId);

        Assertions.assertEquals(tenantId, context.tenantId());
        Assertions.assertEquals(userId, context.userId());
        Assertions.assertEquals(requestId, context.requestId());
        Assertions.assertEquals(traceId, context.traceId());
    }

    @Test
    @DisplayName("字段允许为 null")
    void fieldsAllowNullValues() {
        AppContext context = new AppContext(null, null, null, null);

        Assertions.assertNull(context.tenantId());
        Assertions.assertNull(context.userId());
        Assertions.assertNull(context.requestId());
        Assertions.assertNull(context.traceId());
    }

    @Test
    @DisplayName("record 的 equals 和 hashCode 应正确工作")
    void equalsAndHashCodeShouldWorkProperly() {
        AppContext context1 = new AppContext("t", "u", "r", "tr");
        AppContext context2 = new AppContext("t", "u", "r", "tr");
        AppContext context3 = new AppContext("t2", "u", "r", "tr");

        Assertions.assertEquals(context1, context2);
        Assertions.assertEquals(context1.hashCode(), context2.hashCode());
        Assertions.assertNotEquals(context1, context3);
    }

    @Test
    @DisplayName("toString 应包含所有字段信息")
    void toStringShouldContainAllFields() {
        AppContext context = new AppContext("t", "u", "r", "tr");
        String str = context.toString();
        Assertions.assertTrue(str.contains("tenantId=t"));
        Assertions.assertTrue(str.contains("userId=u"));
        Assertions.assertTrue(str.contains("requestId=r"));
        Assertions.assertTrue(str.contains("traceId=tr"));
    }
}