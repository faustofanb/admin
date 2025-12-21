package io.github.faustofan.admin.system.infrastructure.reponsitory;

import io.github.faustofan.admin.system.domain.model.SysRole;
import io.github.faustofan.admin.system.domain.model.SysRoleTable;
import io.github.faustofan.admin.system.domain.model.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

/**
 * 系统角色仓库
 * 提供对 SysRole 实体的数据库操作方法
 */
@Repository
public interface SysRoleRepository extends JRepository<SysRole, Long> {

    SysRoleTable table = Tables.SYS_ROLE_TABLE;

}
