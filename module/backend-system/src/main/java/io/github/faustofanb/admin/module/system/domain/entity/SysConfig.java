package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntity;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Key;
import org.jetbrains.annotations.Nullable;

/**
 * 参数配置表
 */
@Entity
public interface SysConfig extends BaseEntity {

    /**
     * 参数名称
     */
    String configName();

    /**
     * 参数键名
     */
    @Key
    String configKey();

    /**
     * 参数键值
     */
    String configValue();

    /**
     * 系统内置（Y是 N否）
     */
    int configType();

    /**
     * 备注
     */
    @Nullable
    String remark();
}
