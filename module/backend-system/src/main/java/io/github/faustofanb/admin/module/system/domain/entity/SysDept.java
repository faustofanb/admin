package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.TreeEntity;
import org.babyfish.jimmer.sql.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * 部门表
 */
@Entity
public interface SysDept extends TreeEntity<SysDept> {

    /**
     * 部门名称
     */
    String deptName();

    /**
     * 负责人
     */
    @Nullable
    String leader();

    /**
     * 联系电话
     */
    @Nullable
    String phone();

    /**
     * 邮箱
     */
    @Nullable
    String email();

    /**
     * 部门状态（0正常 1停用）
     */
    int status();

    /**
     * 祖级列表
     */
    @Nullable
    String ancestors();
}
