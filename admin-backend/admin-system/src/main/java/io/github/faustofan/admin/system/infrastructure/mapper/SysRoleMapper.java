package io.github.faustofan.admin.system.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    /**
     * 查找缺失的角色ID
     *
     * @param ids 角色ID列表
     * @return 缺失的角色ID列表
     */
    List<Long> findMissingRoleIds(@Param("ids") List<Long> ids);
}
