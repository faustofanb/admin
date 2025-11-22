package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Boolean;
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
        type = SysMenu.class
)
public class SysMenuTable extends AbstractTypedTable<SysMenu> implements SysMenuProps {
    public static final SysMenuTable $ = new SysMenuTable();

    public SysMenuTable() {
        super(SysMenu.class);
    }

    public SysMenuTable(AbstractTypedTable.DelayedOperation<SysMenu> delayedOperation) {
        super(SysMenu.class, delayedOperation);
    }

    public SysMenuTable(TableImplementor<SysMenu> table) {
        super(table);
    }

    protected SysMenuTable(SysMenuTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public PropExpression.Num<Long> id() {
        return __get(SysMenuProps.ID.unwrap());
    }

    @Override
    public PropExpression<LocalDateTime> createdTime() {
        return __get(SysMenuProps.CREATED_TIME.unwrap());
    }

    @Override
    public PropExpression<LocalDateTime> updatedTime() {
        return __get(SysMenuProps.UPDATED_TIME.unwrap());
    }

    @Override
    public PropExpression<Long> createdBy() {
        return __get(SysMenuProps.CREATED_BY.unwrap());
    }

    @Override
    public PropExpression<Long> updatedBy() {
        return __get(SysMenuProps.UPDATED_BY.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> sort() {
        return __get(SysMenuProps.SORT.unwrap());
    }

    @Override
    public SysMenuTable parent() {
        __beforeJoin();
        if (raw != null) {
            return new SysMenuTable(raw.joinImplementor(SysMenuProps.PARENT.unwrap()));
        }
        return new SysMenuTable(joinOperation(SysMenuProps.PARENT.unwrap()));
    }

    @Override
    public SysMenuTable parent(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysMenuTable(raw.joinImplementor(SysMenuProps.PARENT.unwrap(), joinType));
        }
        return new SysMenuTable(joinOperation(SysMenuProps.PARENT.unwrap(), joinType));
    }

    @Override
    public PropExpression.Num<Long> parentId() {
        return __getAssociatedId(SysMenuProps.PARENT.unwrap());
    }

    @Override
    public Predicate children(Function<SysMenuTableEx, Predicate> block) {
        return exists(SysMenuProps.CHILDREN.unwrap(), block);
    }

    @Override
    public PropExpression.Str menuName() {
        return __get(SysMenuProps.MENU_NAME.unwrap());
    }

    @Override
    public PropExpression.Str menuType() {
        return __get(SysMenuProps.MENU_TYPE.unwrap());
    }

    @Override
    public PropExpression.Str path() {
        return __get(SysMenuProps.PATH.unwrap());
    }

    @Override
    public PropExpression.Str component() {
        return __get(SysMenuProps.COMPONENT.unwrap());
    }

    @Override
    public PropExpression.Str perms() {
        return __get(SysMenuProps.PERMS.unwrap());
    }

    @Override
    public PropExpression.Str icon() {
        return __get(SysMenuProps.ICON.unwrap());
    }

    @Override
    public PropExpression<Boolean> visible() {
        return __get(SysMenuProps.VISIBLE.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> status() {
        return __get(SysMenuProps.STATUS.unwrap());
    }

    @Override
    public Predicate roles(Function<SysRoleTableEx, Predicate> block) {
        return exists(SysMenuProps.ROLES.unwrap(), block);
    }

    @Override
    public SysMenuTableEx asTableEx() {
        return new SysMenuTableEx(this, null);
    }

    @Override
    public SysMenuTable __disableJoin(String reason) {
        return new SysMenuTable(this, reason);
    }

    @GeneratedBy(
            type = SysMenu.class
    )
    public static class Remote extends AbstractTypedTable<SysMenu> {
        public Remote(AbstractTypedTable.DelayedOperation delayedOperation) {
            super(SysMenu.class, delayedOperation);
        }

        public Remote(TableImplementor<SysMenu> table) {
            super(table);
        }

        public PropExpression.Num<Long> id() {
            return __get(SysMenuProps.ID.unwrap());
        }

        @Override
        @Deprecated
        public TableEx<SysMenu> asTableEx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Remote __disableJoin(String reason) {
            return this;
        }
    }
}
