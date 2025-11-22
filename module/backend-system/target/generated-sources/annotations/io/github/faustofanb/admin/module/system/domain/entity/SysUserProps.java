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
        type = SysUser.class
)
@PropsFor(SysUser.class)
public interface SysUserProps extends BaseEntityProps {
    TypedProp.Scalar<SysUser, Long> ID = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("id"));

    TypedProp.Scalar<SysUser, LocalDateTime> CREATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("createdTime"));

    TypedProp.Scalar<SysUser, LocalDateTime> UPDATED_TIME = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("updatedTime"));

    TypedProp.Scalar<SysUser, Long> CREATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("createdBy"));

    TypedProp.Scalar<SysUser, Long> UPDATED_BY = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("updatedBy"));

    TypedProp.Scalar<SysUser, String> USER_NAME = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("userName"));

    TypedProp.Scalar<SysUser, String> NICK_NAME = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("nickName"));

    TypedProp.Scalar<SysUser, String> EMAIL = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("email"));

    TypedProp.Scalar<SysUser, String> PHONENUMBER = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("phonenumber"));

    TypedProp.Scalar<SysUser, String> SEX = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("sex"));

    TypedProp.Scalar<SysUser, String> AVATAR = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("avatar"));

    TypedProp.Scalar<SysUser, String> PASSWORD = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("password"));

    TypedProp.Scalar<SysUser, Integer> STATUS = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("status"));

    TypedProp.Scalar<SysUser, String> LOGIN_IP = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("loginIp"));

    TypedProp.Scalar<SysUser, LocalDateTime> LOGIN_DATE = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("loginDate"));

    TypedProp.Reference<SysUser, SysDept> DEPT = 
        TypedProp.reference(ImmutableType.get(SysUser.class).getProp("dept"));

    TypedProp.ReferenceList<SysUser, SysRole> ROLES = 
        TypedProp.referenceList(ImmutableType.get(SysUser.class).getProp("roles"));

    TypedProp.ReferenceList<SysUser, SysPost> POSTS = 
        TypedProp.referenceList(ImmutableType.get(SysUser.class).getProp("posts"));

    TypedProp.Scalar<SysUser, String> REMARK = 
        TypedProp.scalar(ImmutableType.get(SysUser.class).getProp("remark"));

    PropExpression.Str userName();

    PropExpression.Str nickName();

    PropExpression.Str email();

    PropExpression.Str phonenumber();

    PropExpression.Str sex();

    PropExpression.Str avatar();

    PropExpression.Str password();

    PropExpression.Num<Integer> status();

    PropExpression.Str loginIp();

    PropExpression.Tp<LocalDateTime> loginDate();

    SysDeptTable dept();

    SysDeptTable dept(JoinType joinType);

    PropExpression.Num<Long> deptId();

    Predicate roles(Function<SysRoleTableEx, Predicate> block);

    Predicate posts(Function<SysPostTableEx, Predicate> block);

    PropExpression.Str remark();
}
