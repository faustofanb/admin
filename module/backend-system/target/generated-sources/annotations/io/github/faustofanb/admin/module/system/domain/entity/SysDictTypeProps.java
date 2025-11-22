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
        type = SysDictType.class
)
@PropsFor(SysDictType.class)
public interface SysDictTypeProps extends BaseEntityProps {
    TypedProp.Scalar<SysDictType, Long> ID = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("id"));

    TypedProp.Scalar<SysDictType, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("createdTime"));

    TypedProp.Scalar<SysDictType, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("updatedTime"));

    TypedProp.Scalar<SysDictType, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("createdBy"));

    TypedProp.Scalar<SysDictType, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("updatedBy"));

    TypedProp.Scalar<SysDictType, String> NAME = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("name"));

    TypedProp.Scalar<SysDictType, String> TYPE = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("type"));

    TypedProp.Scalar<SysDictType, Integer> STATUS = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("status"));

    TypedProp.Scalar<SysDictType, String> REMARK = 
        TypedProp.scalar(ImmutableType.get(SysDictType.class).getProp("remark"));

    PropExpression.Str name();

    PropExpression.Str type();

    PropExpression.Num<Integer> status();

    PropExpression.Str remark();
}
