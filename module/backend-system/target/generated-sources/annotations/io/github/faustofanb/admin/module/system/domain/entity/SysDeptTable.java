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
        type = SysDept.class
)
public class SysDeptTable extends AbstractTypedTable<SysDept> implements SysDeptProps {
    public static final SysDeptTable $ = new SysDeptTable();

    public SysDeptTable() {
        super(SysDept.class);
    }

    public SysDeptTable(AbstractTypedTable.DelayedOperation<SysDept> delayedOperation) {
        super(SysDept.class, delayedOperation);
    }

    public SysDeptTable(TableImplementor<SysDept> table) {
        super(table);
    }

    protected SysDeptTable(SysDeptTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public PropExpression.Num<Long> id() {
        return __get(SysDeptProps.ID.unwrap());
    }

    @Override
    public PropExpression<LocalDateTime> createdTime() {
        return __get(SysDeptProps.CREATED_TIME.unwrap());
    }

    @Override
    public PropExpression<LocalDateTime> updatedTime() {
        return __get(SysDeptProps.UPDATED_TIME.unwrap());
    }

    @Override
    public PropExpression<Long> createdBy() {
        return __get(SysDeptProps.CREATED_BY.unwrap());
    }

    @Override
    public PropExpression<Long> updatedBy() {
        return __get(SysDeptProps.UPDATED_BY.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> sort() {
        return __get(SysDeptProps.SORT.unwrap());
    }

    @Override
    public SysDeptTable parent() {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTable(raw.joinImplementor(SysDeptProps.PARENT.unwrap()));
        }
        return new SysDeptTable(joinOperation(SysDeptProps.PARENT.unwrap()));
    }

    @Override
    public SysDeptTable parent(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTable(raw.joinImplementor(SysDeptProps.PARENT.unwrap(), joinType));
        }
        return new SysDeptTable(joinOperation(SysDeptProps.PARENT.unwrap(), joinType));
    }

    @Override
    public PropExpression.Num<Long> parentId() {
        return __getAssociatedId(SysDeptProps.PARENT.unwrap());
    }

    @Override
    public Predicate children(Function<SysDeptTableEx, Predicate> block) {
        return exists(SysDeptProps.CHILDREN.unwrap(), block);
    }

    @Override
    public PropExpression.Str deptName() {
        return __get(SysDeptProps.DEPT_NAME.unwrap());
    }

    @Override
    public PropExpression.Str leader() {
        return __get(SysDeptProps.LEADER.unwrap());
    }

    @Override
    public PropExpression.Str phone() {
        return __get(SysDeptProps.PHONE.unwrap());
    }

    @Override
    public PropExpression.Str email() {
        return __get(SysDeptProps.EMAIL.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> status() {
        return __get(SysDeptProps.STATUS.unwrap());
    }

    @Override
    public PropExpression.Str ancestors() {
        return __get(SysDeptProps.ANCESTORS.unwrap());
    }

    @Override
    public SysDeptTableEx asTableEx() {
        return new SysDeptTableEx(this, null);
    }

    @Override
    public SysDeptTable __disableJoin(String reason) {
        return new SysDeptTable(this, reason);
    }

    @GeneratedBy(
            type = SysDept.class
    )
    public static class Remote extends AbstractTypedTable<SysDept> {
        public Remote(AbstractTypedTable.DelayedOperation delayedOperation) {
            super(SysDept.class, delayedOperation);
        }

        public Remote(TableImplementor<SysDept> table) {
            super(table);
        }

        public PropExpression.Num<Long> id() {
            return __get(SysDeptProps.ID.unwrap());
        }

        @Override
        @Deprecated
        public TableEx<SysDept> asTableEx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Remote __disableJoin(String reason) {
            return this;
        }
    }
}
