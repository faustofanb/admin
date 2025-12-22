package io.github.faustofan.admin.shared.messaging.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.TypedMessageBuilder;
import org.apache.pulsar.shade.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.faustofan.admin.shared.messaging.constants.MsgConstants;

/**
 * 基于 Pulsar 的消息发送实现
 * <p>
 * 负责将系统消息异步发送到 Pulsar 消息队列。
 * 实现了 Producer 的缓存机制，避免重复创建连接以提升性能。
 * 支持消息序列化、事件时间、类型信息和自定义 Header 注入。
 */
public class PulsarMessageProvider implements RemoteMessageProvider {

    /** 日志记录器 */
    private static final Logger log = LoggerFactory.getLogger(PulsarMessageProvider.class);

    /** Pulsar 客户端实例 */
    private final PulsarClient pulsarClient;
    /** Jackson 对象序列化器 */
    private final ObjectMapper objectMapper;
    /** Producer 缓存，Key 为 Topic 名称 */
    private final Map<String, Producer<byte[]>> producerCache = new ConcurrentHashMap<>();

    /**
     * 构造方法，注入 PulsarClient 和 ObjectMapper
     *
     * @param pulsarClient Pulsar 客户端
     * @param objectMapper Jackson 序列化器
     */
    public PulsarMessageProvider(PulsarClient pulsarClient, ObjectMapper objectMapper) {
        this.pulsarClient = pulsarClient;
        this.objectMapper = objectMapper;
    }

    /**
     * 异步发送消息到 Pulsar
     * <p>
     * 自动序列化消息内容，注入类型信息和自定义 Header，返回消息 ID。
     *
     * @param message 系统消息对象
     * @param <T>     消息载荷类型
     * @return 异步发送结果，返回 Pulsar 消息 ID
     */
    @Override
    public <T> CompletableFuture<String> sendAsync(SysMessage<T> message) {
        String fullTopic = MsgConstants.DEFAULT_NAMESPACE + message.topic();

        // 获取或创建 Producer，保证线程安全
        Producer<byte[]> producer = producerCache.computeIfAbsent(fullTopic, this::createProducer);

        // 序列化消息载荷
        byte[] payloadBytes;
        try {
            payloadBytes = objectMapper.writeValueAsBytes(message.payload());
        } catch (JsonProcessingException e) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Serialization failed", e));
        }

        // 构建消息并发送
        TypedMessageBuilder<byte[]> builder = producer.newMessage()
                .key(message.id())
                .value(payloadBytes)
                .eventTime(message.timestamp().toEpochMilli())
                .property("javaType", message.payload().getClass().getName()); // 类型信息用于反序列化

        // 注入 Header
        if (message.headers() != null) {
            message.headers().forEach(builder::property);
        }

        return builder.sendAsync().thenApply(MessageId::toString);
    }

    /**
     * 创建 Pulsar Producer
     * <p>
     * 用于指定 Topic，设置超时和队列满时阻塞。
     *
     * @param topic Topic 名称
     * @return Producer 实例
     */
    private Producer<byte[]> createProducer(String topic) {
        try {
            return pulsarClient.newProducer(Schema.BYTES)
                    .topic(topic)
                    .blockIfQueueFull(true)
                    .sendTimeout(3, java.util.concurrent.TimeUnit.SECONDS)
                    .create();
        } catch (PulsarClientException e) {
            throw new RuntimeException("Failed to create pulsar producer for topic: " + topic, e);
        }
    }
}