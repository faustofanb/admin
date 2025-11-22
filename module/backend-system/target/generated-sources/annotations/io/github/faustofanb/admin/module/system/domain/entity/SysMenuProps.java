package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntityProps;
import java.lang.Boolean;
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
        type = SysMenu.class
)
@PropsFor(SysMenu.class)
public interface SysMenuProps extends BaseEntityProps {
    TypedProp.Scalar<SysMenu, Long> ID = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("id"));

    TypedProp.Scalar<SysMenu, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("createdTime"));

    TypedProp.Scalar<SysMenu, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("updatedTime"));

    TypedProp.Scalar<SysMenu, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("createdBy"));

    TypedProp.Scalar<SysMenu, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("updatedBy"));

    TypedProp.Scalar<SysMenu, Long> PARENT_ID = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("parentId"));

    TypedProp.Scalar<SysMenu, Integer> SORT = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("sort"));

    TypedProp.Reference<SysMenu, SysMenu> PARENT = 
        TypedProp.reference(ImmutableType.get(SysMenu.class).getProp("parent"));

    TypedProp.ReferenceList<SysMenu, SysMenu> CHILDREN = 
        TypedProp.referenceList(ImmutableType.get(SysMenu.class).getProp("children"));

    TypedProp.Scalar<SysMenu, String> MENU_NAME = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("menuName"));

    TypedProp.Scalar<SysMenu, String> MENU_TYPE = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("menuType"));

    TypedProp.Scalar<SysMenu, String> PATH = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("path"));

    TypedProp.Scalar<SysMenu, String> COMPONENT = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("component"));

    TypedProp.Scalar<SysMenu, String> PERMS = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("perms"));

    TypedProp.Scalar<SysMenu, String> ICON = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("icon"));

    TypedProp.Scalar<SysMenu, Boolean> VISIBLE = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("visible"));

    TypedProp.Scalar<SysMenu, Integer> STATUS = 
        TypedProp.scalar(ImmutableType.get(SysMenu.class).getProp("status"));

    TypedProp.ReferenceList<SysMenu, SysRole> ROLES = 
        TypedProp.referenceList(ImmutableType.get(SysMenu.class).getProp("roles"));

    PropExpression.Num<Integer> sort();

    SysMenuTable parent();

    SysMenuTable parent(JoinType joinType);

    PropExpression.Num<Long> parentId();

    Predicate children(Function<SysMenuTableEx, Predicate> block);

    PropExpression.Str menuName();

    PropExpression.Str menuType();

    PropExpression.Str path();

    PropExpression.Str component();

    PropExpression.Str perms();

    PropExpression.Str icon();

    PropExpression<Boolean> visible();

    PropExpression.Num<Integer> status();

    Predicate roles(Function<SysRoleTableEx, Predicate> block);
}
