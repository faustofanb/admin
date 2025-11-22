package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.function.Function;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.impl.table.TableImplementor;
import org.babyfish.jimmer.sql.ast.table.TableEx;
import org.babyfish.jimmer.sql.ast.table.spi.AbstractTypedTable;

@GeneratedBy(
        type = SysRole.class
)
public class SysRoleTable extends AbstractTypedTable<SysRole> implements SysRoleProps {
    public static final SysRoleTable $ = new SysRoleTable();

    public SysRoleTable() {
        super(SysRole.class);
    }

    public SysRoleTable(AbstractTypedTable.DelayedOperation<SysRole> delayedOperation) {
        super(SysRole.class, delayedOperation);
    }

    public SysRoleTable(TableImplementor<SysRole> table) {
        super(table);
    }

    protected SysRoleTable(SysRoleTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public PropExpression.Num<Long> id() {
        return __get(SysRoleProps.ID.unwrap());
    }

    @Override
    public PropExpression<LocalDateTime> createdTime() {
        return __get(SysRoleProps.CREATED_TIME.unwrap());
    }

    @Override
    public PropExpression<LocalDateTime> updatedTime() {
        return __get(SysRoleProps.UPDATED_TIME.unwrap());
    }

    @Override
    public PropExpression<Long> createdBy() {
        return __get(SysRoleProps.CREATED_BY.unwrap());
    }

    @Override
    public PropExpression<Long> updatedBy() {
        return __get(SysRoleProps.UPDATED_BY.unwrap());
    }

    @Override
    public PropExpression.Str roleName() {
        return __get(SysRoleProps.ROLE_NAME.unwrap());
    }

    @Override
    public PropExpression.Str roleKey() {
        return __get(SysRoleProps.ROLE_KEY.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> roleSort() {
        return __get(SysRoleProps.ROLE_SORT.unwrap());
    }

    @Override
    public PropExpression.Str dataScope() {
        return __get(SysRoleProps.DATA_SCOPE.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> status() {
        return __get(SysRoleProps.STATUS.unwrap());
    }

    @Override
    public PropExpression.Str remark() {
        return __get(SysRoleProps.REMARK.unwrap());
    }

    @Override
    public Predicate menus(Function<SysMenuTableEx, Predicate> block) {
        return exists(SysRoleProps.MENUS.unwrap(), block);
    }

    @Override
    public Predicate depts(Function<SysDeptTableEx, Predicate> block) {
        return exists(SysRoleProps.DEPTS.unwrap(), block);
    }

    @Override
    public Predicate users(Function<SysUserTableEx, Predicate> block) {
        return exists(SysRoleProps.USERS.unwrap(), block);
    }

    @Override
    public SysRoleTableEx asTableEx() {
        return new SysRoleTableEx(this, null);
    }

    @Override
    public SysRoleTable __disableJoin(String reason) {
        return new SysRoleTable(this, reason);
    }

    @GeneratedBy(
            type = SysRole.class
    )
    public static class Remote extends AbstractTypedTable<SysRole> {
        public Remote(AbstractTypedTable.DelayedOperation delayedOperation) {
            super(SysRole.class, delayedOperation);
        }

        public Remote(TableImplementor<SysRole> table) {
            super(table);
        }

        public PropExpression.Num<Long> id() {
            return __get(SysRoleProps.ID.unwrap());
        }

        @Override
        @Deprecated
        public TableEx<SysRole> asTableEx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Remote __disableJoin(String reason) {
            return this;
        }
    }
}
