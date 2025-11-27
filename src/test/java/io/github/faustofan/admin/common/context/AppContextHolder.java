package io.github.faustofan.admin.common.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AppContextHolder 单元测试")
class AppContextHolderTest {

    @Test
    @DisplayName("set 和 get 应正确存取上下文")
    void setAndGetShouldStoreAndRetrieveContext() {
        AppContext context = new AppContext("tenant", "user", "req", "trace");
        AppContextHolder.set(context);
        Assertions.assertEquals(context, AppContextHolder.get());
        AppContextHolder.clear();
    }

    @Test
    @DisplayName("getContext 应返回 Optional 包装的上下文")
    void getContextShouldReturnOptional() {
        AppContext context = new AppContext("t", "u", "r", "tr");
        AppContextHolder.set(context);
        Assertions.assertTrue(AppContextHolder.getContext().isPresent());
        Assertions.assertEquals(context, AppContextHolder.getContext().get());
        AppContextHolder.clear();
    }

    @Test
    @DisplayName("clear 后 get 应返回 null")
    void clearShouldRemoveContext() {
        AppContext context = new AppContext("t", "u", "r", "tr");
        AppContextHolder.set(context);
        AppContextHolder.clear();
        Assertions.assertNull(AppContextHolder.get());
        Assertions.assertTrue(AppContextHolder.getContext().isEmpty());
    }

    @Test
    @DisplayName("默认情况下 get 应返回 null")
    void getShouldReturnNullIfNotSet() {
        AppContextHolder.clear();
        Assertions.assertNull(AppContextHolder.get());
        Assertions.assertTrue(AppContextHolder.getContext().isEmpty());
    }

    @Test
    @DisplayName("子线程应继承主线程的上下文")
    void childThreadShouldInheritContext() throws Exception {
        AppContext context = new AppContext("t", "u", "r", "tr");
        AppContextHolder.set(context);
        final AppContext[] childContext = new AppContext[1];
        Thread thread = new Thread(() -> childContext[0] = AppContextHolder.get());
        thread.start();
        thread.join();
        Assertions.assertEquals(context, childContext[0]);
        AppContextHolder.clear();
    }
}