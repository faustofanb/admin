package io.github.faustofan.admin.auth.application.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.event.EventListener;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.github.faustofan.admin.shared.cache.constants.CacheKeys;
import io.github.faustofan.admin.shared.cache.util.CacheUtils;
import io.github.faustofan.admin.shared.messaging.core.PulsarMessageProvider;
import io.github.faustofan.admin.shared.messaging.core.SysMessage;
import io.github.faustofan.admin.system.domain.constants.SysUserTopics;
import io.github.faustofan.admin.system.domain.event.UserPasswordChangedEvent;
import io.github.faustofan.admin.system.domain.event.UserStatusChangedEvent;

/**
 * 鉴权模块缓存一致性监听器
 * 优化后：Zero-DB-Lookup，高性能纯内存处理
 */
@Component
public class AuthUserEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuthUserEventListener.class);

    private final CacheUtils cacheUtils;

    // 预处理 Key 前缀 (去掉 SpEL 的单引号)
    private static final String PREFIX_ID = CacheKeys.AUTH_KEY_ID.replace("'", "");
    private static final String PREFIX_NAME = CacheKeys.AUTH_KEY_NAME.replace("'", "");

    public AuthUserEventListener(CacheUtils cacheUtils) {
        this.cacheUtils = cacheUtils;
    }

    /**
     * 场景 1: 本地监听 (密码变更)
     */
    @EventListener
    @Async("msgVirtualExecutor")
    public void onLocalPasswordChange(SysMessage<UserPasswordChangedEvent> message) {
        if (SysUserTopics.USER_PASSWORD_CHANGED.equals(message.topic())) {
            var event = message.payload();
            // 直接使用事件中的数据
            evictAuthCache(event.userId(), event.tenantId(), event.username());
        }
    }

    /**
     * 场景 2: 远程监听 (状态变更)
     */
    @PulsarListener(topics = SysUserTopics.USER_STATUS_CHANGED, subscriptionName = "auth-user-cache-evict-sub")
    @ConditionalOnBean(PulsarMessageProvider.class)
    public void onRemoteStatusChange(UserStatusChangedEvent event) {
        log.info("Remote status change: user={} status={}", event.username(), event.newStatus());
        // 直接使用事件中的数据
        evictAuthCache(event.userId(), event.tenantId(), event.username());
    }

    /**
     * 纯计算逻辑，无 I/O 阻塞
     */
    private void evictAuthCache(Long userId, Long tenantId, String username) {
        // 1. 构建 ID 维度 Key -> ID:1001
        String idKey = PREFIX_ID + userId;

        // 2. 构建 Username 维度 Key -> NAME:1001:admin
        // 注意：这里一定要判空，尽管 tenantId 通常不为空，但在非多租户模式下需注意
        String safeTenantId = (tenantId == null) ? "0" : tenantId.toString();
        String nameKey = PREFIX_NAME + safeTenantId + ":" + username;

        // 3. 执行清理
        cacheUtils.evict(CacheKeys.AUTH_USER_CACHE, idKey);
        cacheUtils.evict(CacheKeys.AUTH_USER_CACHE, nameKey);

        log.debug("Evicted keys: [{}, {}]", idKey, nameKey);
    }
}