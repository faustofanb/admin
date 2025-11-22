package io.github.faustofanb.admin.core.domain;

import java.lang.Integer;
import java.lang.Long;
import java.time.LocalDateTime;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.meta.ImmutableType;
import org.babyfish.jimmer.meta.TypedProp;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.table.PropsFor;

@GeneratedBy(
        type = TreeEntity.class
)
@PropsFor(TreeEntity.class)
public interface TreeEntityProps extends BaseEntityProps {
    TypedProp.Scalar<TreeEntity, Long> ID = 
        TypedProp.scalar(ImmutableType.get(TreeEntity.class).getProp("id"));

    TypedProp.Scalar<TreeEntity, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(TreeEntity.class).getProp("createdTime"));

    TypedProp.Scalar<TreeEntity, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(TreeEntity.class).getProp("updatedTime"));

    TypedProp.Scalar<TreeEntity, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(TreeEntity.class).getProp("createdBy"));

    TypedProp.Scalar<TreeEntity, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(TreeEntity.class).getProp("updatedBy"));

    TypedProp.Scalar<TreeEntity, Integer> SORT = 
        TypedProp.scalar(ImmutableType.get(TreeEntity.class).getProp("sort"));

    PropExpression.Num<Integer> sort();
}
