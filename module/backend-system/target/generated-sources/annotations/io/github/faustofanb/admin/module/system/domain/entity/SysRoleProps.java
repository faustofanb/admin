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
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.table.PropsFor;

@GeneratedBy(
        type = SysRole.class
)
@PropsFor(SysRole.class)
public interface SysRoleProps extends BaseEntityProps {
    TypedProp.Scalar<SysRole, Long> ID = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("id"));

    TypedProp.Scalar<SysRole, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("createdTime"));

    TypedProp.Scalar<SysRole, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("updatedTime"));

    TypedProp.Scalar<SysRole, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("createdBy"));

    TypedProp.Scalar<SysRole, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("updatedBy"));

    TypedProp.Scalar<SysRole, String> ROLE_NAME = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("roleName"));

    TypedProp.Scalar<SysRole, String> ROLE_KEY = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("roleKey"));

    TypedProp.Scalar<SysRole, Integer> ROLE_SORT = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("roleSort"));

    TypedProp.Scalar<SysRole, String> DATA_SCOPE = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("dataScope"));

    TypedProp.Scalar<SysRole, Integer> STATUS = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("status"));

    TypedProp.Scalar<SysRole, String> REMARK = 
        TypedProp.scalar(ImmutableType.get(SysRole.class).getProp("remark"));

    TypedProp.ReferenceList<SysRole, SysMenu> MENUS = 
        TypedProp.referenceList(ImmutableType.get(SysRole.class).getProp("menus"));

    TypedProp.ReferenceList<SysRole, SysDept> DEPTS = 
        TypedProp.referenceList(ImmutableType.get(SysRole.class).getProp("depts"));

    TypedProp.ReferenceList<SysRole, SysUser> USERS = 
        TypedProp.referenceList(ImmutableType.get(SysRole.class).getProp("users"));

    PropExpression.Str roleName();

    PropExpression.Str roleKey();

    PropExpression.Num<Integer> roleSort();

    PropExpression.Str dataScope();

    PropExpression.Num<Integer> status();

    PropExpression.Str remark();

    Predicate menus(Function<SysMenuTableEx, Predicate> block);

    Predicate depts(Function<SysDeptTableEx, Predicate> block);

    Predicate users(Function<SysUserTableEx, Predicate> block);
}
