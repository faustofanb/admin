package io.github.faustofan.admin.system.domain.service;

import io.github.faustofan.admin.system.infrastructure.reponsitory.SysOrgRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

/**
 * 组织机构领域服务
 * 提供与组织机构相关的领域逻辑操作
 */
@Service
public class OrgDomainService {

    private final SysOrgRepository sysOrgRepository;

    public OrgDomainService(SysOrgRepository sysOrgRepository) {
        this.sysOrgRepository = sysOrgRepository;
    }

    /**
     * 检查组织机构是否存在
     *
     * @param id 组织机构ID
     * @return 如果组织机构存在则返回 true，否则返回 false
     */
    public boolean existsById(@NonNull Long id) {
        return sysOrgRepository.existsById(id);
    }
}
