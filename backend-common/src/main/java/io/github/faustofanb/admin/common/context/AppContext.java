package io.github.faustofanb.admin.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;

import java.util.Set;

@Data
public class AppContext {
    private static final ThreadLocal<AppContext> LOCAL = new TransmittableThreadLocal<>();

    private Long tenantId;
    private Long userId;
    private String username;
    private Set<String> roles;
    private String traceId;

    public static AppContext getContext() {
        return LOCAL.get();
    }

    public static void setContext(AppContext context) {
        LOCAL.set(context);
    }

    public static void clear() {
        LOCAL.remove();
    }
}
