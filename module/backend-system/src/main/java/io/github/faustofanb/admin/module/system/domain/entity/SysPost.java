package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntity;
import org.babyfish.jimmer.sql.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * 岗位表
 */
@Entity
public interface SysPost extends BaseEntity {

    /**
     * 岗位编码
     */
    String postCode();

    /**
     * 岗位名称
     */
    String postName();

    /**
     * 显示顺序
     */
    int sort();

    /**
     * 状态（0正常 1停用）
     */
    int status();

    /**
     * 备注
     */
    @Nullable
    String remark();
}
