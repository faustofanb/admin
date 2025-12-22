package io.github.faustofan.admin.shared.messaging.core;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 无操作的消息发送实现
 * 当没有配置任何远程消息提供者时使用
 */
public class NoOpMessageProvider implements RemoteMessageProvider {
    private static final Logger log = LoggerFactory.getLogger(NoOpMessageProvider.class);

    @Override
    public <T> CompletableFuture<String> sendAsync(SysMessage<T> message) {
        // 1. 打印日志模拟发送
        log.info("【Pulsar Disabled】模拟异步发送消息: {}", message);

        // 2. 关键点：返回一个“立即成功”的 Future，并带有一个假 ID
        // 这样外层的 .thenAccept(msgId -> ...) 就会接收到这个 ID 并正常执行
        return CompletableFuture.completedFuture("mock-msg-id-" + UUID.randomUUID().toString());
    }
}
