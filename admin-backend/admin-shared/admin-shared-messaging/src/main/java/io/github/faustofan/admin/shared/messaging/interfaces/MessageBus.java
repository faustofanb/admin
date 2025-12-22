package io.github.faustofan.admin.shared.messaging.interfaces;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import io.github.faustofan.admin.shared.messaging.core.RemoteMessageProvider;
import io.github.faustofan.admin.shared.messaging.core.SysMessage;

/**
 * 系统消息总线门面
 * <p>
 * 设计模式：事件总线（Event Bus Pattern）+ 异步组合（Asynchronous Composition）
 * 负责消息的本地和远程分发，支持虚拟线程异步处理，提升系统解耦与扩展性。
 */
@Component
public class MessageBus {

    /** 日志记录器 */
    private static final Logger log = LoggerFactory.getLogger(MessageBus.class);

    /** Spring 本地事件发布器 */
    private final ApplicationEventPublisher localPublisher;
    /** 远程消息提供者（如 Pulsar、Kafka 等） */
    private final RemoteMessageProvider remoteProvider;
    /** 虚拟线程异步任务执行器 */
    private final AsyncTaskExecutor virtualExecutor;

    /**
     * 构造方法，注入依赖
     *
     * @param localPublisher  Spring 本地事件发布器
     * @param remoteProvider  远程消息提供者
     * @param virtualExecutor 虚拟线程异步任务执行器
     */
    public MessageBus(
            ApplicationEventPublisher localPublisher,
            RemoteMessageProvider remoteProvider,
            @Qualifier("msgVirtualExecutor") AsyncTaskExecutor virtualExecutor) {
        this.localPublisher = localPublisher;
        this.remoteProvider = remoteProvider;
        this.virtualExecutor = virtualExecutor;
    }

    /**
     * 发送消息的统一入口
     * <p>
     * 使用虚拟线程异步分发消息，不阻塞调用方线程。
     *
     * @param message 待分发的系统消息
     * @param <T>     消息载荷类型
     */
    public <T> void publish(SysMessage<T> message) {
        virtualExecutor.execute(() -> dispatch(message));
    }

    /**
     * 路由分发逻辑
     * <p>
     * 根据消息作用域（LOCAL/REMOTE/GLOBAL）分发到不同通道。
     *
     * @param message 待分发的系统消息
     * @param <T>     消息载荷类型
     */
    private <T> void dispatch(SysMessage<T> message) {
        try {
            switch (message.scope()) {
                case LOCAL -> sendLocal(message);
                case REMOTE -> sendRemote(message);
                case GLOBAL -> {
                    // 并行执行本地和远程分发，互不影响
                    CompletableFuture.allOf(
                            CompletableFuture.runAsync(() -> sendLocal(message), virtualExecutor),
                            sendRemoteAsync(message)).join();
                }
            }
        } catch (Exception e) {
            log.error("Failed to dispatch message: {}", message.id(), e);
            // 可对接死信队列或告警服务
        }
    }

    /**
     * 本地消息分发
     * <p>
     * 通过 Spring 事件机制同步发布消息。
     *
     * @param message 系统消息
     * @param <T>     消息载荷类型
     */
    private <T> void sendLocal(SysMessage<T> message) {
        localPublisher.publishEvent(message);
        log.debug("Local published: {}", message.id());
    }

    /**
     * 远程消息分发（阻塞虚拟线程，等待结果）
     *
     * @param message 系统消息
     * @param <T>     消息载荷类型
     */
    private <T> void sendRemote(SysMessage<T> message) {
        sendRemoteAsync(message).join();
    }

    /**
     * 远程消息分发（异步）
     * <p>
     * 通过远程消息提供者异步发送消息，支持回调处理结果。
     *
     * @param message 系统消息
     * @param <T>     消息载荷类型
     * @return 异步处理结果
     */
    private <T> CompletableFuture<Void> sendRemoteAsync(SysMessage<T> message) {
        return remoteProvider.sendAsync(message)
                .thenAccept(msgId -> log.info("Remote published success. ID: {}, PulsarID: {}", message.id(), msgId))
                .exceptionally(ex -> {
                    log.error("Remote publish failed. ID: {}", message.id(), ex);
                    return null;
                });
    }
}