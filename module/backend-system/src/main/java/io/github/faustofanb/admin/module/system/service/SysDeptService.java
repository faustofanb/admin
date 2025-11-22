package io.github.faustofanb.admin.module.system.service;

import io.github.faustofanb.admin.module.system.domain.entity.SysDept;

import java.util.List;

public interface SysDeptService {

    /**
     * 查询部门树结构
     *
     * @param dept 部门信息
     * @return 部门树
     */
    List<SysDept> selectDeptTree(SysDept dept);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门ID
     */
    void checkDeptDataScope(Long deptId);

    /**
     * 校验部门是否有子部门
     *
     * @param deptId 部门ID
     * @return 结果
     */
    boolean hasChildByDeptId(Long deptId);

    /**
     * 校验部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    boolean checkDeptExistUser(Long deptId);
}
