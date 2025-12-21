package io.github.faustofan.admin.system.infrastructure.reponsitory;

import io.github.faustofan.admin.shared.common.dto.PageRequestDto;
import io.github.faustofan.admin.system.domain.model.SysUser;
import io.github.faustofan.admin.system.domain.model.SysUserTable;
import io.github.faustofan.admin.system.domain.model.Tables;
import io.github.faustofan.admin.system.domain.enums.UserStatus;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;
import io.github.faustofan.admin.system.dto.SysUserLoginView;
import io.github.faustofan.admin.system.dto.SysUserView;
import io.github.faustofan.admin.system.dto.SysUserSearchQuery;

import java.util.Optional;

/**
 * 系统用户仓库
 * 提供对 SysUser 实体的数据库操作方法
 */
@Repository
public interface SysUserRepository extends JRepository<SysUser, Long> {

    SysUserTable table = Tables.SYS_USER_TABLE;

    /**
     * 根据用户名查询用户
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 根据用户名和租户ID查询用户（用于登录认证）
     */
    default SysUserLoginView findByUsernameWithRoles(String username, Long tenantId) {
        return sql().createQuery(table)
                .where(table.username().eq(username))
                .where(table.tenantId().eq(tenantId))
                .where(table.status().eq(UserStatus.ACTIVE))
                .select(table.fetch(SysUserLoginView.class))
                .fetchOneOrNull();
    }

    /**
     * 根据用户ID查询用户（用于 Token 刷新）
     */
    default SysUserLoginView findByIdWithRoles(Long userId) {
        return sql().createQuery(table)
                .where(table.id().eq(userId))
                .select(table.fetch(SysUserLoginView.class))
                .fetchOneOrNull();
    }

    /**
     * 分页查询用户列表
     */
    default Page<SysUserView> findPage(SysUserSearchQuery query, PageRequestDto page) {
        return sql().createQuery(table)
                .where(query)
                .select(table.fetch(SysUserView.class))
                .fetchPage(page.page(), page.size());
    }

    /**
     * 检查用户名是否已存在
     */
    default boolean existsByUsernameAndTenantId(String username, Long tenantId) {
        return sql().createQuery(table)
                .where(table.username().eq(username))
                .where(table.tenantId().eq(tenantId))
                .select(table.id())
                .fetchOneOrNull() != null;
    }

    /**
     * 检查用户是否包含指定角色
     */
    default boolean checkHasRole(Long userId, String roleCode) {
        long count = sql().createQuery(table)
                .where(table.id().eq(userId))
                .where(table.roles(role -> role.code().eqIf(roleCode)))
                .select(table.id().count())
                .fetchFirst();
        return count > 0;
    }
}
