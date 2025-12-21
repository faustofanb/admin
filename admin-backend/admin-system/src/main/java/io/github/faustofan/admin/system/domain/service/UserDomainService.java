package io.github.faustofan.admin.system.domain.service;

import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import io.github.faustofan.admin.shared.common.exception.BizException;
import io.github.faustofan.admin.shared.common.exception.CommonErrorCode;
import io.github.faustofan.admin.shared.common.exception.UserErrorCode;
import io.github.faustofan.admin.shared.common.exception.UserException;
import io.github.faustofan.admin.shared.messaging.MessageBus;
import io.github.faustofan.admin.shared.messaging.MsgScope;
import io.github.faustofan.admin.shared.messaging.SysMessage;
import io.github.faustofan.admin.system.domain.constants.SysUserTopics;
import io.github.faustofan.admin.system.domain.enums.UserStatus;
import io.github.faustofan.admin.system.domain.event.UserCreatedEvent;
import io.github.faustofan.admin.system.domain.event.UserPasswordChangedEvent;
import io.github.faustofan.admin.system.domain.event.UserStatusChangedEvent;
import io.github.faustofan.admin.system.domain.model.Immutables;
import io.github.faustofan.admin.system.domain.model.SysUser;
import io.github.faustofan.admin.system.domain.model.SysUserProps;
import io.github.faustofan.admin.system.infrastructure.reponsitory.SysUserRepository;
import org.babyfish.jimmer.ImmutableObjects;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * 用户领域服务
 * <p>
 * 负责聚合根的生命周期管理、状态流转和事件分发。
 */
@Service
public class UserDomainService {

    /** 用户仓储接口 */
    private final SysUserRepository userRepository;
    /** 消息总线，用于事件分发 */
    private final MessageBus messageBus;
    /** 密码加密器 */
    private final PasswordEncoder passwordEncoder;

    /**
     * 构造方法，注入依赖
     *
     * @param userRepository 用户仓储
     * @param messageBus 消息总线
     * @param passwordEncoder 密码加密器
     */
    public UserDomainService(
            SysUserRepository userRepository,
            MessageBus messageBus,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.messageBus = messageBus;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================================================================
    // 1. 业务规则校验区 (原充血方法的替代品)
    // ========================================================================

    /**
     * 校验用户是否激活
     * <p>
     * 若用户未激活则抛出异常。
     *
     * @param user 用户实体
     */
    public void validateActive(SysUser user) {
        if (user.status() != UserStatus.ACTIVE) {
            throw new BizException(UserErrorCode.USER_NOT_EXIST_OR_DISABLED);
        }
    }

    /**
     * 校验是否允许修改密码
     * <p>
     * 若用户被锁定则抛出异常。
     *
     * @param user 用户实体
     */
    public void validateCanChangePassword(SysUser user) {
        if (user.status() == UserStatus.LOCKED) {
            throw new BizException(UserErrorCode.PWD_CHANGE_UNSUPPORTED);
        }
    }

    /**
     * 判断用户是否拥有指定角色
     * <p>
     * 支持属性未加载时的安全检查。
     *
     * @param user 用户实体
     * @param roleCode 角色编码
     * @return 是否拥有角色
     */
    public boolean hasRole(SysUser user, String roleCode) {
        // Jimmer 技巧：判断属性是否已加载
        if(ImmutableObjects.isLoaded(user, SysUserProps.ROLES)) {
            return user.roles().stream()
                    .anyMatch(r -> Objects.equals(r.code(), roleCode));
        }

        // 如果没加载，为了避免 LazyException，直接查库 (使用 count 1 优化)
        return userRepository.checkHasRole(user.id(), roleCode);
    }

    // ========================================================================
    // 2. 状态变更动作区 (Action & Events)
    // ========================================================================

    /**
     * 创建用户
     * <p>
     * 包含业务校验、密码加密、持久化和事件发布。
     *
     * @param sysUser 用户实体
     * @return 持久化后的用户实体
     */
    @Transactional
    public SysUser createUser(SysUser sysUser) {
        // 1. 业务规则校验 (Invariant)
        if (userRepository.existsByUsernameAndTenantId(sysUser.username(), sysUser.tenantId())) {
            throw new UserException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }

        // 2. 密码加密处理
        SysUser finalUser = Immutables.createSysUser(sysUser, draft -> {
            String encodedPwd = passwordEncoder.encode(sysUser.password());
            draft.setPassword(encodedPwd);
        });

        // 3. 持久化 (Persistence)
        SysUser savedUser = userRepository.save(finalUser);

        // 4. 构建并发布领域事件 (Event Publishing)
        var event = new UserCreatedEvent(
                savedUser.id(),
                savedUser.tenantId(),
                savedUser.username(),
                savedUser.nickname(),
                savedUser.org().id(),
                Instant.now()
        );

        messageBus.publish(SysMessage.<UserCreatedEvent>builder()
                .topic(SysUserTopics.USER_CREATED)
                .scope(MsgScope.GLOBAL) // 既通知本地缓存，也通知下游服务
                .payload(event)
                .build()
        );

        return savedUser;
    }

    /**
     * 变更用户状态（禁用/解禁）
     * <p>
     * 包含状态校验、持久化和事件发布。
     *
     * @param username 用户名
     * @param newStatus 新状态
     * @param reason 变更原因
     */
    @Transactional
    public void changeStatus(String username, UserStatus newStatus, String reason) {
        // 1. 获取聚合根
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));

        // 2. 状态校验 (利用 Entity 自身的充血方法或直接比较)
        if (user.status() == newStatus) {
            return; // 幂等处理
        }
        if (user.isSuperAdmin() && newStatus == UserStatus.LOCKED) {
            throw new BizException(CommonErrorCode.OPERATION_NOT_ALLOWED);
        }

        // 3. 变更状态 (Jimmer Draft Update)
        userRepository.save(Immutables.createSysUser(draft -> {
            draft.setId(user.id());
            draft.setStatus(newStatus);
        }));

        // 4. 发布状态变更事件
        var event = new UserStatusChangedEvent(
                user.id(),
                user.tenantId(),
                user.username(),
                user.status(), // old status
                newStatus,     // new status
                reason
        );

        messageBus.publish(SysMessage.<UserStatusChangedEvent>builder()
                .topic(SysUserTopics.USER_STATUS_CHANGED)
                .scope(MsgScope.GLOBAL) // 通知网关剔除Token等
                .payload(event)
                .headers(Map.of("operator", AppContextHolder.getContext().username())) // 附加元数据
                .build());
    }

    /**
     * 修改用户密码
     * <p>
     * 包含业务校验、加密、持久化和事件发布。
     *
     * @param username 用户名
     * @param newPassword 新密码
     */
    @Transactional
    public void changePassword(String username, String newPassword) {
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow();

        // 调用实体内部的校验逻辑
        validateCanChangePassword(user);

        userRepository.save(Immutables.createSysUser( draft -> {
            draft.setId(user.id());
            draft.setPassword(passwordEncoder.encode(newPassword));
        }));

        // 发送安全事件 (建议 Scope.REMOTE 或 GLOBAL 发给审计服务)
        var event = new UserPasswordChangedEvent(user.id(), user.tenantId(), username, Instant.now());

        messageBus.publish(SysMessage.<UserPasswordChangedEvent>builder()
                .topic(SysUserTopics.USER_PASSWORD_CHANGED)
                .scope(MsgScope.GLOBAL)
                .payload(event)
                .build());
    }
}