package io.github.faustofan.admin.system.infrastructure.reponsitory;

import io.github.faustofan.admin.system.domain.model.SysOrg;
import io.github.faustofan.admin.system.domain.model.SysOrgTable;
import io.github.faustofan.admin.system.domain.model.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

/**
 * 系统组织机构仓库
 * 提供对 SysOrg 实体的数据库操作方法
 */
@Repository
public interface SysOrgRepository extends JRepository<SysOrg, Long> {

    SysOrgTable table = Tables.SYS_ORG_TABLE;

    /**
     * 检查组织机构是否存在
     *
     * @param id 组织机构ID
     * @return 如果组织机构存在则返回 true，否则返回 false
     */
    boolean existsById(@NonNull Long id);
}
