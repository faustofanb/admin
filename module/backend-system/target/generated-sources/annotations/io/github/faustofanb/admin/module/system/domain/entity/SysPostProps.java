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
        type = SysPost.class
)
@PropsFor(SysPost.class)
public interface SysPostProps extends BaseEntityProps {
    TypedProp.Scalar<SysPost, Long> ID = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("id"));

    TypedProp.Scalar<SysPost, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("createdTime"));

    TypedProp.Scalar<SysPost, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("updatedTime"));

    TypedProp.Scalar<SysPost, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("createdBy"));

    TypedProp.Scalar<SysPost, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("updatedBy"));

    TypedProp.Scalar<SysPost, String> POST_CODE = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("postCode"));

    TypedProp.Scalar<SysPost, String> POST_NAME = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("postName"));

    TypedProp.Scalar<SysPost, Integer> SORT = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("sort"));

    TypedProp.Scalar<SysPost, Integer> STATUS = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("status"));

    TypedProp.Scalar<SysPost, String> REMARK = 
        TypedProp.scalar(ImmutableType.get(SysPost.class).getProp("remark"));

    PropExpression.Str postCode();

    PropExpression.Str postName();

    PropExpression.Num<Integer> sort();

    PropExpression.Num<Integer> status();

    PropExpression.Str remark();
}
