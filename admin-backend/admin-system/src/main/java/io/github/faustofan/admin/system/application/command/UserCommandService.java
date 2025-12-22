package io.github.faustofan.admin.system.application.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;

import io.github.faustofan.admin.shared.common.exception.BizException;
import io.github.faustofan.admin.shared.common.exception.errcode.BizErrorCode;
import io.github.faustofan.admin.system.domain.model.SysUser;
import io.github.faustofan.admin.system.domain.service.OrgDomainService;
import io.github.faustofan.admin.system.domain.service.RoleDomainService;
import io.github.faustofan.admin.system.domain.service.TenantDomainService;
import io.github.faustofan.admin.system.domain.service.UserDomainService;
import io.github.faustofan.admin.system.dto.SysUserCreateCommand;

/**
 * 用户命令服务
 * <p>
 * 负责处理用户相关的应用层命令，如新增用户等。
 */
@Service
public class UserCommandService {

    /** 用户领域服务，封装用户相关的领域逻辑 */
    private final UserDomainService userDomainService;

    private final TenantDomainService tenantDomainService;

    private final OrgDomainService orgDomainService;

    private final RoleDomainService roleDomainService;

    /**
     * 构造方法，注入用户领域服务
     *
     * @param userDomainService 用户领域服务
     */
    public UserCommandService(
            UserDomainService userDomainService, TenantDomainService tenantDomainService,
            OrgDomainService orgDomainService,
            RoleDomainService roleDomainService) {
        this.userDomainService = userDomainService;
        this.tenantDomainService = tenantDomainService;
        this.orgDomainService = orgDomainService;
        this.roleDomainService = roleDomainService;
    }

    /**
     * 新增用户
     * <p>
     * 根据传入的用户创建命令参数，构建用户领域对象并持久化。
     * 密码为明文，实际加密逻辑在领域服务中处理。
     * 支持设置用户所属组织和角色列表。
     *
     * @param cmd 用户创建命令参数
     * @return 新增用户的主键ID
     */
    @Transactional
    public Long create(SysUserCreateCommand cmd) {
        // 校验租户、组织和角色是否存在
        if (!tenantDomainService.existsById(cmd.getTenantId()))
            throw new BizException(BizErrorCode.BIND_TENANT_NOT_EXIST);
        if (!orgDomainService.existsById(cmd.getOrgId()))
            throw new BizException(BizErrorCode.BIND_ORG_NOT_EXIST);
        // 获取不存在的角色ID列表
        List<Long> notExistRoleIds = roleDomainService.existsById(cmd.getRoleIds());
        // 如果有不存在的角色ID，则抛出异常
        if (!notExistRoleIds.isEmpty())
            throw new BizException(
                    BizErrorCode.BIND_ROLE_NOT_EXIST,
                    "以下角色ID不存在：" + Joiner.on(",").join(notExistRoleIds));

        SysUser savedUser = userDomainService.createUser(cmd.toEntity());
        return savedUser.id();
    }
}