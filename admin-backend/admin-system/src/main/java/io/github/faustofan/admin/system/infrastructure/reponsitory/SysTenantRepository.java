package io.github.faustofan.admin.system.infrastructure.reponsitory;

import io.github.faustofan.admin.system.domain.model.SysTenant;
import io.github.faustofan.admin.system.domain.model.SysTenantTable;
import io.github.faustofan.admin.system.domain.model.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

/**
 * 系统租户仓库
 * 提供对 SysTenant 实体的数据库操作方法
 */
@Repository
public interface SysTenantRepository extends JRepository<SysTenant, Long> {
    // 系统租户表
    SysTenantTable table = Tables.SYS_TENANT_TABLE;

    /**
     * 检查租户是否存在
     *
     * @param id 租户ID
     * @return 如果租户存在则返回 true，否则返回 false
     */
    boolean existsById(@NonNull Long id);

}
