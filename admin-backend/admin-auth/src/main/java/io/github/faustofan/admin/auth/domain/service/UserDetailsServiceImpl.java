package io.github.faustofan.admin.auth.domain.service;

import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.faustofan.admin.auth.domain.model.LoginUser;
import io.github.faustofan.admin.shared.cache.constants.CacheKeys;
import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import io.github.faustofan.admin.shared.common.exception.BizException;
import io.github.faustofan.admin.shared.common.exception.UserException;
import io.github.faustofan.admin.shared.common.exception.errcode.BizErrorCode;
import io.github.faustofan.admin.shared.common.exception.errcode.UserErrorCode;
import io.github.faustofan.admin.shared.distributed.constants.RedisKeyRegistry;
import io.github.faustofan.admin.shared.distributed.core.RedisUtil;
import io.github.faustofan.admin.system.domain.enums.UserStatus;
import io.github.faustofan.admin.system.dto.SysUserLoginView;
import io.github.faustofan.admin.system.infrastructure.reponsitory.SysUserRepository;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户详情服务
 * 实现 Spring Security 的 UserDetailsService 接口
 */
@Service
@CacheConfig(cacheNames = CacheKeys.CACHE_AUTH_USER)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SysUserRepository userRepository;

    private final RedisUtil redisUtil;

    public UserDetailsServiceImpl(SysUserRepository userRepository, RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.redisUtil = redisUtil;
    }

    /**
     * 根据用户名加载用户信息
     * Spring Security 标准接口，多租户场景下请使用 loadUserByUsernameAndTenant
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("请使用 loadUserByUsernameAndTenant 方法");
    }

    /**
     * 场景 A: 登录时加载
     * 缓存 Key 格式: name:{tenantId}:{username}
     * sync = true: 防止高并发登录击穿 DB
     */
    @Cacheable(key = CacheKeys.KEY_NAME_SPEL + "+ #tenantId + ':' + #username", sync = true)
    public LoginUser loadUserByUsernameAndTenant(String username, Long tenantId) {
        LoginUser user = loadUserFromDatabase(username, tenantId);
        if (user == null) {
            throw new BizException(BizErrorCode.USER_NOT_EXIST_OR_DISABLED);
        }
        return user;
    }

    /**
     * 场景 B: 刷新 Token / 获取当前用户信息时加载
     * 缓存 Key 格式: id:{userId}
     * 注意：这里和上面是不同的 Key，缓存了两份数据。
     * 权衡：冗余存储换取 O(1) 的读取速度，更新时需要同时清除两份。
     */
    @Cacheable(key = CacheKeys.KEY_ID_SPEL + "+ #userId", sync = true)
    public LoginUser loadUserById(Long userId) {
        var userEntity = userRepository.findByIdWithRoles(userId);
        if (userEntity == null) {
            throw new BizException(BizErrorCode.USER_NOT_EXIST);
        }
        LoginUser user = loadUserFromDatabase(userEntity.getUsername(), userEntity.getTenantId());
        if (user == null) {
            throw new BizException(BizErrorCode.USER_NOT_EXIST);
        }
        return user;
    }

    /**
     * 检查 Token 是否已登出
     * @param token JWT Token
     * @return      true 如果已登出，false 否则 
     */
    public boolean isLogOut(String token) {
        return redisUtil.isMemberOfSet(RedisKeyRegistry.SEC_BLACKLIST, "TOKENS", token);
    }

    /**
     * 从数据库加载用户信息
     */
    private LoginUser loadUserFromDatabase(String username, Long tenantId) {
        var user = userRepository.findByUsernameWithRoles(username, tenantId);
        if (user == null) {
            return null;
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BizException(BizErrorCode.USER_DISABLED);
        }

        var roles = user.getRoles();
        boolean isSuperAdmin = user.isSuperAdmin();

        var roleMaps = roles.stream()
                .collect(Collectors.toUnmodifiableMap(
                        SysUserLoginView.TargetOf_roles::getId,
                        SysUserLoginView.TargetOf_roles::getName
                ));

        var permissions = roles.stream()
                .flatMap(role -> role.getMenus().stream())
                .map(SysUserLoginView.TargetOf_roles.TargetOf_menus::getPermCode)
                .filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());

        var policyContents = roles.stream()
                .flatMap(role -> role.getPolicies().stream())
                .map(SysUserLoginView.TargetOf_roles.TargetOf_policies::getPolicyContent)
                .collect(Collectors.toUnmodifiableSet());

        return new LoginUser(
                user.getId(),
                user.getTenantId(),
                user.getOrg().getId(),
                user.getOrg().getName(),
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                roleMaps,
                permissions,
                policyContents,
                isSuperAdmin,
                user.getStatus() == UserStatus.ACTIVE,
                true,
                true,
                true);
    }
}
