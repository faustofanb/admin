package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntityProps;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.time.LocalDateTime;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.meta.ImmutableType;
import org.babyfish.jimmer.meta.TypedProp;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.table.PropsFor;

@GeneratedBy(
        type = SysConfig.class
)
@PropsFor(SysConfig.class)
public interface SysConfigProps extends BaseEntityProps {
    TypedProp.Scalar<SysConfig, Long> ID = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("id"));

    TypedProp.Scalar<SysConfig, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("createdTime"));

    TypedProp.Scalar<SysConfig, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("updatedTime"));

    TypedProp.Scalar<SysConfig, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("createdBy"));

    TypedProp.Scalar<SysConfig, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("updatedBy"));

    TypedProp.Scalar<SysConfig, String> CONFIG_NAME = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("configName"));

    TypedProp.Scalar<SysConfig, String> CONFIG_KEY = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("configKey"));

    TypedProp.Scalar<SysConfig, String> CONFIG_VALUE = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("configValue"));

    TypedProp.Scalar<SysConfig, Integer> CONFIG_TYPE = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("configType"));

    TypedProp.Scalar<SysConfig, String> REMARK = 
        TypedProp.scalar(ImmutableType.get(SysConfig.class).getProp("remark"));

    PropExpression.Str configName();

    PropExpression.Str configKey();

    PropExpression.Str configValue();

    PropExpression.Num<Integer> configType();

    PropExpression.Str remark();
}
