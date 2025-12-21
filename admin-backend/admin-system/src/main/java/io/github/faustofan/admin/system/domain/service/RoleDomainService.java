package io.github.faustofan.admin.system.domain.service;

import io.github.faustofan.admin.system.infrastructure.mapper.SysRoleMapper;
import io.github.faustofan.admin.system.infrastructure.reponsitory.SysRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色领域服务
 * <p>
 * 负责处理角色相关的领域逻辑。
 */
@Service
public class RoleDomainService {

    private final SysRoleRepository repository;

    private final SysRoleMapper mapper;

    public RoleDomainService(SysRoleRepository repository, SysRoleMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }

    /**
     * 检查角色是否存在
     *
     * @param id 角色ID
     * @return 如果角色存在则返回 true，否则返回 false
     */
    public List<Long> existsById(List<Long> id) {
        return mapper.findMissingRoleIds(id);
    }
}
