package io.github.faustofanb.admin.core.domain;

import java.lang.Long;
import java.time.LocalDateTime;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.meta.ImmutableType;
import org.babyfish.jimmer.meta.TypedProp;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.table.Props;
import org.babyfish.jimmer.sql.ast.table.PropsFor;

@GeneratedBy(
        type = BaseEntity.class
)
@PropsFor(BaseEntity.class)
public interface BaseEntityProps extends Props {
    TypedProp.Scalar<BaseEntity, Long> ID = 
        TypedProp.scalar(ImmutableType.get(BaseEntity.class).getProp("id"));

    TypedProp.Scalar<BaseEntity, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(BaseEntity.class).getProp("createdTime"));

    TypedProp.Scalar<BaseEntity, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(BaseEntity.class).getProp("updatedTime"));

    TypedProp.Scalar<BaseEntity, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(BaseEntity.class).getProp("createdBy"));

    TypedProp.Scalar<BaseEntity, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(BaseEntity.class).getProp("updatedBy"));

    PropExpression.Num<Long> id();

    PropExpression.Tp<LocalDateTime> createdTime();

    PropExpression.Tp<LocalDateTime> updatedTime();

    PropExpression.Num<Long> createdBy();

    PropExpression.Num<Long> updatedBy();
}
