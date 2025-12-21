package io.github.faustofan.admin.system.domain.service;

import io.github.faustofan.admin.system.infrastructure.reponsitory.SysTenantRepository;
import org.springframework.stereotype.Service;

/**
 * 租户领域服务
 * 提供与租户相关的领域逻辑操作
 */
@Service
public class TenantDomainService {

    private final SysTenantRepository sysTenantRepository;

    public  TenantDomainService(SysTenantRepository sysTenantRepository) {
        this.sysTenantRepository = sysTenantRepository;
    }

    /**
     * 检查租户是否存在
     *
     * @param id 租户ID
     * @return 如果租户存在则返回 true，否则返回 false
     */
    public boolean existsById(Long id) {
        return sysTenantRepository.existsById(id);
    }
}
