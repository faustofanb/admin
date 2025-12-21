package io.github.faustofan.admin.system.domain.event;

import io.github.faustofan.admin.system.domain.enums.UserStatus;

import java.io.Serializable;

/**
 * 用户状态变更事件 (如：禁用/解禁)
 */
public record UserStatusChangedEvent(
        Long userId,
        Long tenantId,
        String username,
        UserStatus oldStatus,
        UserStatus newStatus,
        String reason
) implements Serializable {}
