package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntity;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.IdView;
import org.babyfish.jimmer.sql.ManyToOne;
import org.babyfish.jimmer.sql.OneToMany;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 部门表
 */
@Entity
public interface SysDept extends BaseEntity {

    @IdView("parent")
    @Nullable
    Long parentId();

    Integer sort();

    @ManyToOne
    @Nullable
    SysDept parent();

    @OneToMany(mappedBy = "parent")
    List<SysDept> children();

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
