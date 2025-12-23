package io.github.faustofan.admin.system.application.query;

import io.github.faustofan.admin.shared.common.dto.PageRequestDto;
import io.github.faustofan.admin.shared.common.dto.PageResponse;
import io.github.faustofan.admin.system.dto.SysUserSearchQuery;
import io.github.faustofan.admin.system.dto.SysUserView;
import io.github.faustofan.admin.system.infrastructure.reponsitory.SysUserRepository;
import org.babyfish.jimmer.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 用户查询服务
 * 提供用户相关的查询操作
 */
@Service
public class UserQueryService {

    private final SysUserRepository sysUserRepository;

    public UserQueryService(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    /**
     * 列出用户，支持分页和查询条件
     *
     * @param pageRequest 分页请求参数
     * @param query       用户搜索查询参数
     * @return 分页的用户视图列表
     */
    public Page<SysUserView> listUsers(Pageable pageRequest, SysUserSearchQuery query) {
        return sysUserRepository.findPage(query, pageRequest);
    }
}
