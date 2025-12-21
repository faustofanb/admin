package io.github.faustofan.admin.shared.messaging;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 统一系统消息体
 *
 * @param id        消息唯一ID (幂等性依据)
 * @param topic     业务主题
 * @param scope     传播范围
 * @param payload   业务数据 (需可序列化)
 * @param headers   元数据 (TraceId, UserContext等)
 * @param timestamp 这是产生时间
 * @param <T>       载荷类型
 */
public record SysMessage<T>(
        String id,
        String topic,
        MsgScope scope,
        T payload,
        Map<String, String> headers,
        Instant timestamp
) implements Serializable {

    // 紧凑构造函数进行防御性校验
    public SysMessage {
        Objects.requireNonNull(topic, "Topic cannot be null");
        Objects.requireNonNull(scope, "Scope cannot be null");
        Objects.requireNonNull(payload, "Payload cannot be null");
    }

    /**
     * 静态流式构建器 (Functional Style)
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private String topic;
        private MsgScope scope = MsgScope.LOCAL; // 默认本地
        private T payload;
        private Map<String, String> headers = Map.of();

        public Builder<T> topic(String topic) { this.topic = topic; return this; }
        public Builder<T> scope(MsgScope scope) { this.scope = scope; return this; }
        public Builder<T> payload(T payload) { this.payload = payload; return this; }
        public Builder<T> headers(Map<String, String> headers) { this.headers = headers; return this; }

        public SysMessage<T> build() {
            return new SysMessage<>(
                    UUID.randomUUID().toString(),
                    topic,
                    scope,
                    payload,
                    headers,
                    Instant.now()
            );
        }
    }

    // 快速转换载荷类型
    public <R> SysMessage<R> mapPayload(java.util.function.Function<T, R> mapper) {
        return new SysMessage<>(id, topic, scope, mapper.apply(payload), headers, timestamp);
    }
}
