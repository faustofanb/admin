package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntityProps;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.time.LocalDateTime;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.meta.ImmutableType;
import org.babyfish.jimmer.meta.TypedProp;
import org.babyfish.jimmer.sql.JoinType;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.table.PropsFor;

@GeneratedBy(
        type = SysDictData.class
)
@PropsFor(SysDictData.class)
public interface SysDictDataProps extends BaseEntityProps {
    TypedProp.Scalar<SysDictData, Long> ID = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("id"));

    TypedProp.Scalar<SysDictData, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("createdTime"));

    TypedProp.Scalar<SysDictData, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("updatedTime"));

    TypedProp.Scalar<SysDictData, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("createdBy"));

    TypedProp.Scalar<SysDictData, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("updatedBy"));

    TypedProp.Scalar<SysDictData, Integer> SORT = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("sort"));

    TypedProp.Scalar<SysDictData, String> LABEL = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("label"));

    TypedProp.Scalar<SysDictData, String> VALUE = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("value"));

    TypedProp.Reference<SysDictData, SysDictType> DICT_TYPE = 
        TypedProp.reference(ImmutableType.get(SysDictData.class).getProp("dictType"));

    TypedProp.Scalar<SysDictData, String> CSS_CLASS = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("cssClass"));

    TypedProp.Scalar<SysDictData, String> LIST_CLASS = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("listClass"));

    TypedProp.Scalar<SysDictData, Integer> STATUS = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("status"));

    TypedProp.Scalar<SysDictData, String> REMARK = 
        TypedProp.scalar(ImmutableType.get(SysDictData.class).getProp("remark"));

    PropExpression.Num<Integer> sort();

    PropExpression.Str label();

    PropExpression.Str value();

    SysDictTypeTable dictType();

    SysDictTypeTable dictType(JoinType joinType);

    PropExpression.Num<Long> dictTypeId();

    PropExpression.Str cssClass();

    PropExpression.Str listClass();

    PropExpression.Num<Integer> status();

    PropExpression.Str remark();
}
