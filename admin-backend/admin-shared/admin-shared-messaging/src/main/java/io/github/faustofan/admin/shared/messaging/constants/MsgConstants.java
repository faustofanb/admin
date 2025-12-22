package io.github.faustofan.admin.shared.messaging.constants;

/** 消息相关常量 */
public final class MsgConstants {
    private MsgConstants() {
    }

    /** 默认租户与命名空间 */
    public static final String DEFAULT_NAMESPACE = "persistent://public/default/";

    /** 链路追踪 Key */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    /** 默认重试次数 */
    public static final int MAX_RETRY_ATTEMPTS = 3;
}
