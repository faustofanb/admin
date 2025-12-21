package io.github.faustofan.admin.shared.messaging;

import java.util.concurrent.CompletableFuture;

/**
 * 远程消息发送器 SPI
 * 函数式接口，方便 Mock 和 Lambda 注入
 */
@FunctionalInterface
public interface RemoteMessageProvider {
    /**
     * 异步发送消息
     */
    <T> CompletableFuture<String> sendAsync(SysMessage<T> message);
}
