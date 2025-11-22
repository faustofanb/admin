package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.function.Function;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.sql.JoinType;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.impl.table.TableImplementor;
import org.babyfish.jimmer.sql.ast.table.TableEx;
import org.babyfish.jimmer.sql.ast.table.spi.AbstractTypedTable;

@GeneratedBy(
        type = SysUser.class
)
public class SysUserTable extends AbstractTypedTable<SysUser> implements SysUserProps {
    public static final SysUserTable $ = new SysUserTable();

    public SysUserTable() {
        super(SysUser.class);
    }

    public SysUserTable(AbstractTypedTable.DelayedOperation<SysUser> delayedOperation) {
        super(SysUser.class, delayedOperation);
    }

    public SysUserTable(TableImplementor<SysUser> table) {
        super(table);
    }

    protected SysUserTable(SysUserTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public PropExpression.Num<Long> id() {
        return __get(SysUserProps.ID.unwrap());
    }

    @Override
    public PropExpression<LocalDateTime> createdTime() {
        return __get(SysUserProps.CREATED_TIME.unwrap());
    }

    @Override
    public PropExpression<LocalDateTime> updatedTime() {
        return __get(SysUserProps.UPDATED_TIME.unwrap());
    }

    @Override
    public PropExpression<Long> createdBy() {
        return __get(SysUserProps.CREATED_BY.unwrap());
    }

    @Override
    public PropExpression<Long> updatedBy() {
        return __get(SysUserProps.UPDATED_BY.unwrap());
    }

    @Override
    public PropExpression.Str userName() {
        return __get(SysUserProps.USER_NAME.unwrap());
    }

    @Override
    public PropExpression.Str nickName() {
        return __get(SysUserProps.NICK_NAME.unwrap());
    }

    @Override
    public PropExpression.Str email() {
        return __get(SysUserProps.EMAIL.unwrap());
    }

    @Override
    public PropExpression.Str phonenumber() {
        return __get(SysUserProps.PHONENUMBER.unwrap());
    }

    @Override
    public PropExpression.Str sex() {
        return __get(SysUserProps.SEX.unwrap());
    }

    @Override
    public PropExpression.Str avatar() {
        return __get(SysUserProps.AVATAR.unwrap());
    }

    @Override
    public PropExpression.Str password() {
        return __get(SysUserProps.PASSWORD.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> status() {
        return __get(SysUserProps.STATUS.unwrap());
    }

    @Override
    public PropExpression.Str loginIp() {
        return __get(SysUserProps.LOGIN_IP.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> loginDate() {
        return __get(SysUserProps.LOGIN_DATE.unwrap());
    }

    @Override
    public SysDeptTable dept() {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTable(raw.joinImplementor(SysUserProps.DEPT.unwrap()));
        }
        return new SysDeptTable(joinOperation(SysUserProps.DEPT.unwrap()));
    }

    @Override
    public SysDeptTable dept(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTable(raw.joinImplementor(SysUserProps.DEPT.unwrap(), joinType));
        }
        return new SysDeptTable(joinOperation(SysUserProps.DEPT.unwrap(), joinType));
    }

    @Override
    public PropExpression.Num<Long> deptId() {
        return __getAssociatedId(SysUserProps.DEPT.unwrap());
    }

    @Override
    public Predicate roles(Function<SysRoleTableEx, Predicate> block) {
        return exists(SysUserProps.ROLES.unwrap(), block);
    }

    @Override
    public Predicate posts(Function<SysPostTableEx, Predicate> block) {
        return exists(SysUserProps.POSTS.unwrap(), block);
    }

    @Override
    public PropExpression.Str remark() {
        return __get(SysUserProps.REMARK.unwrap());
    }

    @Override
    public SysUserTableEx asTableEx() {
        return new SysUserTableEx(this, null);
    }

    @Override
    public SysUserTable __disableJoin(String reason) {
        return new SysUserTable(this, reason);
    }

    @GeneratedBy(
            type = SysUser.class
    )
    public static class Remote extends AbstractTypedTable<SysUser> {
        public Remote(AbstractTypedTable.DelayedOperation delayedOperation) {
            super(SysUser.class, delayedOperation);
        }

        public Remote(TableImplementor<SysUser> table) {
            super(table);
        }

        public PropExpression.Num<Long> id() {
            return __get(SysUserProps.ID.unwrap());
        }

        @Override
        @Deprecated
        public TableEx<SysUser> asTableEx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Remote __disableJoin(String reason) {
            return this;
        }
    }
}
