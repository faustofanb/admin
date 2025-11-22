package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntityProps;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.function.Function;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.meta.ImmutableType;
import org.babyfish.jimmer.meta.TypedProp;
import org.babyfish.jimmer.sql.JoinType;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.table.PropsFor;

@GeneratedBy(
        type = SysDept.class
)
@PropsFor(SysDept.class)
public interface SysDeptProps extends BaseEntityProps {
    TypedProp.Scalar<SysDept, Long> ID = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("id"));

    TypedProp.Scalar<SysDept, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("createdTime"));

    TypedProp.Scalar<SysDept, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("updatedTime"));

    TypedProp.Scalar<SysDept, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("createdBy"));

    TypedProp.Scalar<SysDept, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("updatedBy"));

    TypedProp.Scalar<SysDept, Long> PARENT_ID = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("parentId"));

    TypedProp.Scalar<SysDept, Integer> SORT = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("sort"));

    TypedProp.Reference<SysDept, SysDept> PARENT = 
        TypedProp.reference(ImmutableType.get(SysDept.class).getProp("parent"));

    TypedProp.ReferenceList<SysDept, SysDept> CHILDREN = 
        TypedProp.referenceList(ImmutableType.get(SysDept.class).getProp("children"));

    TypedProp.Scalar<SysDept, String> DEPT_NAME = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("deptName"));

    TypedProp.Scalar<SysDept, String> LEADER = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("leader"));

    TypedProp.Scalar<SysDept, String> PHONE = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("phone"));

    TypedProp.Scalar<SysDept, String> EMAIL = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("email"));

    TypedProp.Scalar<SysDept, Integer> STATUS = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("status"));

    TypedProp.Scalar<SysDept, String> ANCESTORS = 
        TypedProp.scalar(ImmutableType.get(SysDept.class).getProp("ancestors"));

    PropExpression.Num<Integer> sort();

    SysDeptTable parent();

    SysDeptTable parent(JoinType joinType);

    PropExpression.Num<Long> parentId();

    Predicate children(Function<SysDeptTableEx, Predicate> block);

    PropExpression.Str deptName();

    PropExpression.Str leader();

    PropExpression.Str phone();

    PropExpression.Str email();

    PropExpression.Num<Integer> status();

    PropExpression.Str ancestors();
}
