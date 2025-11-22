package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntity;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Key;

/**
 * 字典类型表
 */
@Entity
public interface SysDictType extends BaseEntity {

    /**
     * 字典名称
     */
    @Key
    String name();

    /**
     * 字典类型
     */
    @Key
    String type();

    /**
     * 状态（0正常 1停用）
     */
    int status();

    /**
     * 备注
     */
    String remark();
}
