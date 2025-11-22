package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntity;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.ManyToOne;
import org.jetbrains.annotations.Nullable;

/**
 * 字典数据表
 */
@Entity
public interface SysDictData extends BaseEntity {

    /**
     * 字典排序
     */
    int sort();

    /**
     * 字典标签
     */
    String label();

    /**
     * 字典键值
     */
    String value();

    /**
     * 字典类型
     */
    @ManyToOne
    SysDictType dictType();

    /**
     * 样式属性（其他样式扩展）
     */
    @Nullable
    String cssClass();

    /**
     * 表格回显样式
     */
    @Nullable
    String listClass();

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
