package io.github.faustofanb.admin.module.system.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.faustofanb.admin.core.domain.BaseEntityDraft;
import java.io.Serializable;
import java.lang.CloneNotSupportedException;
import java.lang.Cloneable;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.System;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.babyfish.jimmer.CircularReferenceException;
import org.babyfish.jimmer.DraftConsumer;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.UnloadedException;
import org.babyfish.jimmer.client.Description;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.jackson.ImmutableModuleRequiredException;
import org.babyfish.jimmer.lang.OldChain;
import org.babyfish.jimmer.meta.ImmutablePropCategory;
import org.babyfish.jimmer.meta.ImmutableType;
import org.babyfish.jimmer.meta.PropId;
import org.babyfish.jimmer.runtime.DraftContext;
import org.babyfish.jimmer.runtime.DraftSpi;
import org.babyfish.jimmer.runtime.ImmutableSpi;
import org.babyfish.jimmer.runtime.Internal;
import org.babyfish.jimmer.runtime.NonSharedList;
import org.babyfish.jimmer.runtime.Visibility;
import org.babyfish.jimmer.sql.ManyToOne;
import org.babyfish.jimmer.sql.OneToMany;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@GeneratedBy(
        type = SysDept.class
)
public interface SysDeptDraft extends SysDept, BaseEntityDraft {
    SysDeptDraft.Producer $ = Producer.INSTANCE;

    @OldChain
    SysDeptDraft setId(long id);

    @OldChain
    SysDeptDraft setCreatedTime(LocalDateTime createdTime);

    @OldChain
    SysDeptDraft setUpdatedTime(LocalDateTime updatedTime);

    @OldChain
    SysDeptDraft setCreatedBy(Long createdBy);

    @OldChain
    SysDeptDraft setUpdatedBy(Long updatedBy);

    @OldChain
    SysDeptDraft setParentId(Long parentId);

    @OldChain
    SysDeptDraft setSort(Integer sort);

    @Nullable
    SysDeptDraft parent();

    SysDeptDraft parent(boolean autoCreate);

    @OldChain
    SysDeptDraft setParent(SysDept parent);

    @OldChain
    SysDeptDraft applyParent(DraftConsumer<SysDeptDraft> block);

    @OldChain
    SysDeptDraft applyParent(SysDept base, DraftConsumer<SysDeptDraft> block);

    List<SysDeptDraft> children(boolean autoCreate);

    @OldChain
    SysDeptDraft setChildren(List<SysDept> children);

    @OldChain
    SysDeptDraft addIntoChildren(DraftConsumer<SysDeptDraft> block);

    @OldChain
    SysDeptDraft addIntoChildren(SysDept base, DraftConsumer<SysDeptDraft> block);

    @OldChain
    SysDeptDraft setDeptName(String deptName);

    @OldChain
    SysDeptDraft setLeader(String leader);

    @OldChain
    SysDeptDraft setPhone(String phone);

    @OldChain
    SysDeptDraft setEmail(String email);

    @OldChain
    SysDeptDraft setStatus(int status);

    @OldChain
    SysDeptDraft setAncestors(String ancestors);

    @GeneratedBy(
            type = SysDept.class
    )
    class Producer {
        static final Producer INSTANCE = new Producer();

        public static final int SLOT_ID = 0;

        public static final int SLOT_CREATED_TIME = 1;

        public static final int SLOT_UPDATED_TIME = 2;

        public static final int SLOT_CREATED_BY = 3;

        public static final int SLOT_UPDATED_BY = 4;

        public static final int SLOT_PARENT_ID = 5;

        public static final int SLOT_SORT = 6;

        public static final int SLOT_PARENT = 7;

        public static final int SLOT_CHILDREN = 8;

        public static final int SLOT_DEPT_NAME = 9;

        public static final int SLOT_LEADER = 10;

        public static final int SLOT_PHONE = 11;

        public static final int SLOT_EMAIL = 12;

        public static final int SLOT_STATUS = 13;

        public static final int SLOT_ANCESTORS = 14;

        public static final ImmutableType TYPE = ImmutableType
            .newBuilder(
                "0.9.96",
                SysDept.class,
                Collections.singleton(BaseEntityDraft.Producer.TYPE),
                (ctx, base) -> new DraftImpl(ctx, (SysDept)base)
            )
            .redefine("id", SLOT_ID)
            .redefine("createdTime", SLOT_CREATED_TIME)
            .redefine("updatedTime", SLOT_UPDATED_TIME)
            .redefine("createdBy", SLOT_CREATED_BY)
            .redefine("updatedBy", SLOT_UPDATED_BY)
            .add(SLOT_PARENT_ID, "parentId", ImmutablePropCategory.SCALAR, Long.class, true)
            .add(SLOT_SORT, "sort", ImmutablePropCategory.SCALAR, Integer.class, true)
            .add(SLOT_PARENT, "parent", ManyToOne.class, SysDept.class, true)
            .add(SLOT_CHILDREN, "children", OneToMany.class, SysDept.class, false)
            .add(SLOT_DEPT_NAME, "deptName", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_LEADER, "leader", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_PHONE, "phone", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_EMAIL, "email", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_STATUS, "status", ImmutablePropCategory.SCALAR, int.class, false)
            .add(SLOT_ANCESTORS, "ancestors", ImmutablePropCategory.SCALAR, String.class, true)
            .build();

        private Producer() {
        }

        public SysDept produce(DraftConsumer<SysDeptDraft> block) {
            return (SysDept)Internal.produce(TYPE, null, block);
        }

        public SysDept produce(SysDept base, DraftConsumer<SysDeptDraft> block) {
            return (SysDept)Internal.produce(TYPE, base, block);
        }

        public SysDept produce(boolean resolveImmediately, DraftConsumer<SysDeptDraft> block) {
            return (SysDept)Internal.produce(TYPE, null, resolveImmediately, block);
        }

        public SysDept produce(SysDept base, boolean resolveImmediately,
                DraftConsumer<SysDeptDraft> block) {
            return (SysDept)Internal.produce(TYPE, base, resolveImmediately, block);
        }

        /**
         * Class, not interface, for free-marker
         */
        @GeneratedBy(
                type = SysDept.class
        )
        @JsonPropertyOrder({"dummyPropForJacksonError__", "id", "createdTime", "updatedTime", "createdBy", "updatedBy", "parentId", "sort", "parent", "children", "deptName", "leader", "phone", "email", "status", "ancestors"})
        public abstract static class Implementor implements SysDept, ImmutableSpi {
            @Override
            public final Object __get(PropId prop) {
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		return __get(prop.asName());
                    case SLOT_ID:
                    		return (Long)id();
                    case SLOT_CREATED_TIME:
                    		return createdTime();
                    case SLOT_UPDATED_TIME:
                    		return updatedTime();
                    case SLOT_CREATED_BY:
                    		return createdBy();
                    case SLOT_UPDATED_BY:
                    		return updatedBy();
                    case SLOT_PARENT_ID:
                    		return parentId();
                    case SLOT_SORT:
                    		return sort();
                    case SLOT_PARENT:
                    		return parent();
                    case SLOT_CHILDREN:
                    		return children();
                    case SLOT_DEPT_NAME:
                    		return deptName();
                    case SLOT_LEADER:
                    		return leader();
                    case SLOT_PHONE:
                    		return phone();
                    case SLOT_EMAIL:
                    		return email();
                    case SLOT_STATUS:
                    		return (Integer)status();
                    case SLOT_ANCESTORS:
                    		return ancestors();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + prop + "\"");
                }
            }

            @Override
            public final Object __get(String prop) {
                switch (prop) {
                    case "id":
                    		return (Long)id();
                    case "createdTime":
                    		return createdTime();
                    case "updatedTime":
                    		return updatedTime();
                    case "createdBy":
                    		return createdBy();
                    case "updatedBy":
                    		return updatedBy();
                    case "parentId":
                    		return parentId();
                    case "sort":
                    		return sort();
                    case "parent":
                    		return parent();
                    case "children":
                    		return children();
                    case "deptName":
                    		return deptName();
                    case "leader":
                    		return leader();
                    case "phone":
                    		return phone();
                    case "email":
                    		return email();
                    case "status":
                    		return (Integer)status();
                    case "ancestors":
                    		return ancestors();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + prop + "\"");
                }
            }

            public final long getId() {
                return id();
            }

            public final LocalDateTime getCreatedTime() {
                return createdTime();
            }

            public final LocalDateTime getUpdatedTime() {
                return updatedTime();
            }

            public final Long getCreatedBy() {
                return createdBy();
            }

            public final Long getUpdatedBy() {
                return updatedBy();
            }

            @Nullable
            public final Long getParentId() {
                return parentId();
            }

            public final Integer getSort() {
                return sort();
            }

            @Nullable
            public final SysDept getParent() {
                return parent();
            }

            public final List<SysDept> getChildren() {
                return children();
            }

            public final String getDeptName() {
                return deptName();
            }

            @Nullable
            public final String getLeader() {
                return leader();
            }

            @Nullable
            public final String getPhone() {
                return phone();
            }

            @Nullable
            public final String getEmail() {
                return email();
            }

            public final int getStatus() {
                return status();
            }

            @Nullable
            public final String getAncestors() {
                return ancestors();
            }

            @Override
            public final ImmutableType __type() {
                return TYPE;
            }

            public final int getDummyPropForJacksonError__() {
                throw new ImmutableModuleRequiredException();
            }
        }

        @GeneratedBy(
                type = SysDept.class
        )
        private static class Impl extends Implementor implements Cloneable, Serializable {
            private Visibility __visibility;

            long __idValue;

            boolean __idLoaded = false;

            LocalDateTime __createdTimeValue;

            LocalDateTime __updatedTimeValue;

            Long __createdByValue;

            boolean __createdByLoaded = false;

            Long __updatedByValue;

            boolean __updatedByLoaded = false;

            Integer __sortValue;

            boolean __sortLoaded = false;

            SysDept __parentValue;

            boolean __parentLoaded = false;

            NonSharedList<SysDept> __childrenValue;

            String __deptNameValue;

            String __leaderValue;

            boolean __leaderLoaded = false;

            String __phoneValue;

            boolean __phoneLoaded = false;

            String __emailValue;

            boolean __emailLoaded = false;

            int __statusValue;

            boolean __statusLoaded = false;

            String __ancestorsValue;

            boolean __ancestorsLoaded = false;

            Impl() {
                __visibility = Visibility.of(15);
                __visibility.show(SLOT_PARENT_ID, false);
            }

            @Override
            @JsonIgnore
            public long id() {
                if (!__idLoaded) {
                    throw new UnloadedException(SysDept.class, "id");
                }
                return __idValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime createdTime() {
                if (__createdTimeValue == null) {
                    throw new UnloadedException(SysDept.class, "createdTime");
                }
                return __createdTimeValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime updatedTime() {
                if (__updatedTimeValue == null) {
                    throw new UnloadedException(SysDept.class, "updatedTime");
                }
                return __updatedTimeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long createdBy() {
                if (!__createdByLoaded) {
                    throw new UnloadedException(SysDept.class, "createdBy");
                }
                return __createdByValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long updatedBy() {
                if (!__updatedByLoaded) {
                    throw new UnloadedException(SysDept.class, "updatedBy");
                }
                return __updatedByValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long parentId() {
                SysDept __target = parent();
                return __target != null ? __target.id() : null;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Integer sort() {
                if (!__sortLoaded) {
                    throw new UnloadedException(SysDept.class, "sort");
                }
                return __sortValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public SysDept parent() {
                if (!__parentLoaded) {
                    throw new UnloadedException(SysDept.class, "parent");
                }
                return __parentValue;
            }

            @Override
            @JsonIgnore
            public List<SysDept> children() {
                if (__childrenValue == null) {
                    throw new UnloadedException(SysDept.class, "children");
                }
                return __childrenValue;
            }

            @Override
            @JsonIgnore
            @Description("部门名称")
            public String deptName() {
                if (__deptNameValue == null) {
                    throw new UnloadedException(SysDept.class, "deptName");
                }
                return __deptNameValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("负责人")
            public String leader() {
                if (!__leaderLoaded) {
                    throw new UnloadedException(SysDept.class, "leader");
                }
                return __leaderValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("联系电话")
            public String phone() {
                if (!__phoneLoaded) {
                    throw new UnloadedException(SysDept.class, "phone");
                }
                return __phoneValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("邮箱")
            public String email() {
                if (!__emailLoaded) {
                    throw new UnloadedException(SysDept.class, "email");
                }
                return __emailValue;
            }

            @Override
            @JsonIgnore
            @Description("部门状态（0正常 1停用）")
            public int status() {
                if (!__statusLoaded) {
                    throw new UnloadedException(SysDept.class, "status");
                }
                return __statusValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("祖级列表")
            public String ancestors() {
                if (!__ancestorsLoaded) {
                    throw new UnloadedException(SysDept.class, "ancestors");
                }
                return __ancestorsValue;
            }

            @Override
            public Impl clone() {
                try {
                    return (Impl)super.clone();
                } catch(CloneNotSupportedException ex) {
                    throw new AssertionError(ex);
                }
            }

            @Override
            public boolean __isLoaded(PropId prop) {
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		return __isLoaded(prop.asName());
                    case SLOT_ID:
                    		return __idLoaded;
                    case SLOT_CREATED_TIME:
                    		return __createdTimeValue != null;
                    case SLOT_UPDATED_TIME:
                    		return __updatedTimeValue != null;
                    case SLOT_CREATED_BY:
                    		return __createdByLoaded;
                    case SLOT_UPDATED_BY:
                    		return __updatedByLoaded;
                    case SLOT_PARENT_ID:
                    		return __isLoaded(PropId.byIndex(SLOT_PARENT)) && (parent() == null || 
                            	((ImmutableSpi)parent()).__isLoaded(PropId.byIndex(Producer.SLOT_ID)));
                    case SLOT_SORT:
                    		return __sortLoaded;
                    case SLOT_PARENT:
                    		return __parentLoaded;
                    case SLOT_CHILDREN:
                    		return __childrenValue != null;
                    case SLOT_DEPT_NAME:
                    		return __deptNameValue != null;
                    case SLOT_LEADER:
                    		return __leaderLoaded;
                    case SLOT_PHONE:
                    		return __phoneLoaded;
                    case SLOT_EMAIL:
                    		return __emailLoaded;
                    case SLOT_STATUS:
                    		return __statusLoaded;
                    case SLOT_ANCESTORS:
                    		return __ancestorsLoaded;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + prop + "\"");
                }
            }

            @Override
            public boolean __isLoaded(String prop) {
                switch (prop) {
                    case "id":
                    		return __idLoaded;
                    case "createdTime":
                    		return __createdTimeValue != null;
                    case "updatedTime":
                    		return __updatedTimeValue != null;
                    case "createdBy":
                    		return __createdByLoaded;
                    case "updatedBy":
                    		return __updatedByLoaded;
                    case "parentId":
                    		return __isLoaded(PropId.byIndex(SLOT_PARENT)) && (parent() == null || 
                            	((ImmutableSpi)parent()).__isLoaded(PropId.byIndex(Producer.SLOT_ID)));
                    case "sort":
                    		return __sortLoaded;
                    case "parent":
                    		return __parentLoaded;
                    case "children":
                    		return __childrenValue != null;
                    case "deptName":
                    		return __deptNameValue != null;
                    case "leader":
                    		return __leaderLoaded;
                    case "phone":
                    		return __phoneLoaded;
                    case "email":
                    		return __emailLoaded;
                    case "status":
                    		return __statusLoaded;
                    case "ancestors":
                    		return __ancestorsLoaded;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + prop + "\"");
                }
            }

            @Override
            public boolean __isVisible(PropId prop) {
                if (__visibility == null) {
                    return true;
                }
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		return __isVisible(prop.asName());
                    case SLOT_ID:
                    		return __visibility.visible(SLOT_ID);
                    case SLOT_CREATED_TIME:
                    		return __visibility.visible(SLOT_CREATED_TIME);
                    case SLOT_UPDATED_TIME:
                    		return __visibility.visible(SLOT_UPDATED_TIME);
                    case SLOT_CREATED_BY:
                    		return __visibility.visible(SLOT_CREATED_BY);
                    case SLOT_UPDATED_BY:
                    		return __visibility.visible(SLOT_UPDATED_BY);
                    case SLOT_PARENT_ID:
                    		return __visibility.visible(SLOT_PARENT_ID);
                    case SLOT_SORT:
                    		return __visibility.visible(SLOT_SORT);
                    case SLOT_PARENT:
                    		return __visibility.visible(SLOT_PARENT);
                    case SLOT_CHILDREN:
                    		return __visibility.visible(SLOT_CHILDREN);
                    case SLOT_DEPT_NAME:
                    		return __visibility.visible(SLOT_DEPT_NAME);
                    case SLOT_LEADER:
                    		return __visibility.visible(SLOT_LEADER);
                    case SLOT_PHONE:
                    		return __visibility.visible(SLOT_PHONE);
                    case SLOT_EMAIL:
                    		return __visibility.visible(SLOT_EMAIL);
                    case SLOT_STATUS:
                    		return __visibility.visible(SLOT_STATUS);
                    case SLOT_ANCESTORS:
                    		return __visibility.visible(SLOT_ANCESTORS);
                    default: return true;
                }
            }

            @Override
            public boolean __isVisible(String prop) {
                if (__visibility == null) {
                    return true;
                }
                switch (prop) {
                    case "id":
                    		return __visibility.visible(SLOT_ID);
                    case "createdTime":
                    		return __visibility.visible(SLOT_CREATED_TIME);
                    case "updatedTime":
                    		return __visibility.visible(SLOT_UPDATED_TIME);
                    case "createdBy":
                    		return __visibility.visible(SLOT_CREATED_BY);
                    case "updatedBy":
                    		return __visibility.visible(SLOT_UPDATED_BY);
                    case "parentId":
                    		return __visibility.visible(SLOT_PARENT_ID);
                    case "sort":
                    		return __visibility.visible(SLOT_SORT);
                    case "parent":
                    		return __visibility.visible(SLOT_PARENT);
                    case "children":
                    		return __visibility.visible(SLOT_CHILDREN);
                    case "deptName":
                    		return __visibility.visible(SLOT_DEPT_NAME);
                    case "leader":
                    		return __visibility.visible(SLOT_LEADER);
                    case "phone":
                    		return __visibility.visible(SLOT_PHONE);
                    case "email":
                    		return __visibility.visible(SLOT_EMAIL);
                    case "status":
                    		return __visibility.visible(SLOT_STATUS);
                    case "ancestors":
                    		return __visibility.visible(SLOT_ANCESTORS);
                    default: return true;
                }
            }

            @Override
            public int hashCode() {
                int hash = __visibility != null ? __visibility.hashCode() : 0;
                if (__idLoaded) {
                    hash = 31 * hash + Long.hashCode(__idValue);
                    // If entity-id is loaded, return directly
                    return hash;
                }
                if (__createdTimeValue != null) {
                    hash = 31 * hash + __createdTimeValue.hashCode();
                }
                if (__updatedTimeValue != null) {
                    hash = 31 * hash + __updatedTimeValue.hashCode();
                }
                if (__createdByLoaded && __createdByValue != null) {
                    hash = 31 * hash + __createdByValue.hashCode();
                }
                if (__updatedByLoaded && __updatedByValue != null) {
                    hash = 31 * hash + __updatedByValue.hashCode();
                }
                if (__sortLoaded && __sortValue != null) {
                    hash = 31 * hash + __sortValue.hashCode();
                }
                if (__parentLoaded && __parentValue != null) {
                    hash = 31 * hash + __parentValue.hashCode();
                }
                if (__childrenValue != null) {
                    hash = 31 * hash + __childrenValue.hashCode();
                }
                if (__deptNameValue != null) {
                    hash = 31 * hash + __deptNameValue.hashCode();
                }
                if (__leaderLoaded && __leaderValue != null) {
                    hash = 31 * hash + __leaderValue.hashCode();
                }
                if (__phoneLoaded && __phoneValue != null) {
                    hash = 31 * hash + __phoneValue.hashCode();
                }
                if (__emailLoaded && __emailValue != null) {
                    hash = 31 * hash + __emailValue.hashCode();
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__ancestorsLoaded && __ancestorsValue != null) {
                    hash = 31 * hash + __ancestorsValue.hashCode();
                }
                return hash;
            }

            private int __shallowHashCode() {
                int hash = __visibility != null ? __visibility.hashCode() : 0;
                if (__idLoaded) {
                    hash = 31 * hash + Long.hashCode(__idValue);
                }
                if (__createdTimeValue != null) {
                    hash = 31 * hash + System.identityHashCode(__createdTimeValue);
                }
                if (__updatedTimeValue != null) {
                    hash = 31 * hash + System.identityHashCode(__updatedTimeValue);
                }
                if (__createdByLoaded) {
                    hash = 31 * hash + System.identityHashCode(__createdByValue);
                }
                if (__updatedByLoaded) {
                    hash = 31 * hash + System.identityHashCode(__updatedByValue);
                }
                if (__sortLoaded) {
                    hash = 31 * hash + System.identityHashCode(__sortValue);
                }
                if (__parentLoaded) {
                    hash = 31 * hash + System.identityHashCode(__parentValue);
                }
                if (__childrenValue != null) {
                    hash = 31 * hash + System.identityHashCode(__childrenValue);
                }
                if (__deptNameValue != null) {
                    hash = 31 * hash + System.identityHashCode(__deptNameValue);
                }
                if (__leaderLoaded) {
                    hash = 31 * hash + System.identityHashCode(__leaderValue);
                }
                if (__phoneLoaded) {
                    hash = 31 * hash + System.identityHashCode(__phoneValue);
                }
                if (__emailLoaded) {
                    hash = 31 * hash + System.identityHashCode(__emailValue);
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__ancestorsLoaded) {
                    hash = 31 * hash + System.identityHashCode(__ancestorsValue);
                }
                return hash;
            }

            @Override
            public int __hashCode(boolean shallow) {
                return shallow ? __shallowHashCode() : hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null || !(obj instanceof Implementor)) {
                    return false;
                }
                Implementor __other = (Implementor)obj;
                if (__isVisible(PropId.byIndex(SLOT_ID)) != __other.__isVisible(PropId.byIndex(SLOT_ID))) {
                    return false;
                }
                boolean __idLoaded = this.__idLoaded;
                if (__idLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ID))) {
                    return false;
                }
                if (__idLoaded) {
                    // If entity-id is loaded, return directly
                    return __idValue == __other.id();
                }
                if (__isVisible(PropId.byIndex(SLOT_CREATED_TIME)) != __other.__isVisible(PropId.byIndex(SLOT_CREATED_TIME))) {
                    return false;
                }
                boolean __createdTimeLoaded = __createdTimeValue != null;
                if (__createdTimeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CREATED_TIME))) {
                    return false;
                }
                if (__createdTimeLoaded && !Objects.equals(__createdTimeValue, __other.createdTime())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_UPDATED_TIME)) != __other.__isVisible(PropId.byIndex(SLOT_UPDATED_TIME))) {
                    return false;
                }
                boolean __updatedTimeLoaded = __updatedTimeValue != null;
                if (__updatedTimeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_UPDATED_TIME))) {
                    return false;
                }
                if (__updatedTimeLoaded && !Objects.equals(__updatedTimeValue, __other.updatedTime())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CREATED_BY)) != __other.__isVisible(PropId.byIndex(SLOT_CREATED_BY))) {
                    return false;
                }
                boolean __createdByLoaded = this.__createdByLoaded;
                if (__createdByLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CREATED_BY))) {
                    return false;
                }
                if (__createdByLoaded && !Objects.equals(__createdByValue, __other.createdBy())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_UPDATED_BY)) != __other.__isVisible(PropId.byIndex(SLOT_UPDATED_BY))) {
                    return false;
                }
                boolean __updatedByLoaded = this.__updatedByLoaded;
                if (__updatedByLoaded != __other.__isLoaded(PropId.byIndex(SLOT_UPDATED_BY))) {
                    return false;
                }
                if (__updatedByLoaded && !Objects.equals(__updatedByValue, __other.updatedBy())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PARENT_ID)) != __other.__isVisible(PropId.byIndex(SLOT_PARENT_ID))) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_SORT)) != __other.__isVisible(PropId.byIndex(SLOT_SORT))) {
                    return false;
                }
                boolean __sortLoaded = this.__sortLoaded;
                if (__sortLoaded != __other.__isLoaded(PropId.byIndex(SLOT_SORT))) {
                    return false;
                }
                if (__sortLoaded && !Objects.equals(__sortValue, __other.sort())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PARENT)) != __other.__isVisible(PropId.byIndex(SLOT_PARENT))) {
                    return false;
                }
                boolean __parentLoaded = this.__parentLoaded;
                if (__parentLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PARENT))) {
                    return false;
                }
                if (__parentLoaded && !Objects.equals(__parentValue, __other.parent())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CHILDREN)) != __other.__isVisible(PropId.byIndex(SLOT_CHILDREN))) {
                    return false;
                }
                boolean __childrenLoaded = __childrenValue != null;
                if (__childrenLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CHILDREN))) {
                    return false;
                }
                if (__childrenLoaded && !Objects.equals(__childrenValue, __other.children())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DEPT_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_DEPT_NAME))) {
                    return false;
                }
                boolean __deptNameLoaded = __deptNameValue != null;
                if (__deptNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DEPT_NAME))) {
                    return false;
                }
                if (__deptNameLoaded && !Objects.equals(__deptNameValue, __other.deptName())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_LEADER)) != __other.__isVisible(PropId.byIndex(SLOT_LEADER))) {
                    return false;
                }
                boolean __leaderLoaded = this.__leaderLoaded;
                if (__leaderLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LEADER))) {
                    return false;
                }
                if (__leaderLoaded && !Objects.equals(__leaderValue, __other.leader())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PHONE)) != __other.__isVisible(PropId.byIndex(SLOT_PHONE))) {
                    return false;
                }
                boolean __phoneLoaded = this.__phoneLoaded;
                if (__phoneLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PHONE))) {
                    return false;
                }
                if (__phoneLoaded && !Objects.equals(__phoneValue, __other.phone())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_EMAIL)) != __other.__isVisible(PropId.byIndex(SLOT_EMAIL))) {
                    return false;
                }
                boolean __emailLoaded = this.__emailLoaded;
                if (__emailLoaded != __other.__isLoaded(PropId.byIndex(SLOT_EMAIL))) {
                    return false;
                }
                if (__emailLoaded && !Objects.equals(__emailValue, __other.email())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_STATUS)) != __other.__isVisible(PropId.byIndex(SLOT_STATUS))) {
                    return false;
                }
                boolean __statusLoaded = this.__statusLoaded;
                if (__statusLoaded != __other.__isLoaded(PropId.byIndex(SLOT_STATUS))) {
                    return false;
                }
                if (__statusLoaded && __statusValue != __other.status()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ANCESTORS)) != __other.__isVisible(PropId.byIndex(SLOT_ANCESTORS))) {
                    return false;
                }
                boolean __ancestorsLoaded = this.__ancestorsLoaded;
                if (__ancestorsLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ANCESTORS))) {
                    return false;
                }
                if (__ancestorsLoaded && !Objects.equals(__ancestorsValue, __other.ancestors())) {
                    return false;
                }
                return true;
            }

            private boolean __shallowEquals(Object obj) {
                if (obj == null || !(obj instanceof Implementor)) {
                    return false;
                }
                Implementor __other = (Implementor)obj;
                if (__isVisible(PropId.byIndex(SLOT_ID)) != __other.__isVisible(PropId.byIndex(SLOT_ID))) {
                    return false;
                }
                boolean __idLoaded = this.__idLoaded;
                if (__idLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ID))) {
                    return false;
                }
                if (__idLoaded && __idValue != __other.id()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CREATED_TIME)) != __other.__isVisible(PropId.byIndex(SLOT_CREATED_TIME))) {
                    return false;
                }
                boolean __createdTimeLoaded = __createdTimeValue != null;
                if (__createdTimeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CREATED_TIME))) {
                    return false;
                }
                if (__createdTimeLoaded && __createdTimeValue != __other.createdTime()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_UPDATED_TIME)) != __other.__isVisible(PropId.byIndex(SLOT_UPDATED_TIME))) {
                    return false;
                }
                boolean __updatedTimeLoaded = __updatedTimeValue != null;
                if (__updatedTimeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_UPDATED_TIME))) {
                    return false;
                }
                if (__updatedTimeLoaded && __updatedTimeValue != __other.updatedTime()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CREATED_BY)) != __other.__isVisible(PropId.byIndex(SLOT_CREATED_BY))) {
                    return false;
                }
                boolean __createdByLoaded = this.__createdByLoaded;
                if (__createdByLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CREATED_BY))) {
                    return false;
                }
                if (__createdByLoaded && __createdByValue != __other.createdBy()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_UPDATED_BY)) != __other.__isVisible(PropId.byIndex(SLOT_UPDATED_BY))) {
                    return false;
                }
                boolean __updatedByLoaded = this.__updatedByLoaded;
                if (__updatedByLoaded != __other.__isLoaded(PropId.byIndex(SLOT_UPDATED_BY))) {
                    return false;
                }
                if (__updatedByLoaded && __updatedByValue != __other.updatedBy()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PARENT_ID)) != __other.__isVisible(PropId.byIndex(SLOT_PARENT_ID))) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_SORT)) != __other.__isVisible(PropId.byIndex(SLOT_SORT))) {
                    return false;
                }
                boolean __sortLoaded = this.__sortLoaded;
                if (__sortLoaded != __other.__isLoaded(PropId.byIndex(SLOT_SORT))) {
                    return false;
                }
                if (__sortLoaded && __sortValue != __other.sort()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PARENT)) != __other.__isVisible(PropId.byIndex(SLOT_PARENT))) {
                    return false;
                }
                boolean __parentLoaded = this.__parentLoaded;
                if (__parentLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PARENT))) {
                    return false;
                }
                if (__parentLoaded && __parentValue != __other.parent()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CHILDREN)) != __other.__isVisible(PropId.byIndex(SLOT_CHILDREN))) {
                    return false;
                }
                boolean __childrenLoaded = __childrenValue != null;
                if (__childrenLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CHILDREN))) {
                    return false;
                }
                if (__childrenLoaded && __childrenValue != __other.children()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DEPT_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_DEPT_NAME))) {
                    return false;
                }
                boolean __deptNameLoaded = __deptNameValue != null;
                if (__deptNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DEPT_NAME))) {
                    return false;
                }
                if (__deptNameLoaded && __deptNameValue != __other.deptName()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_LEADER)) != __other.__isVisible(PropId.byIndex(SLOT_LEADER))) {
                    return false;
                }
                boolean __leaderLoaded = this.__leaderLoaded;
                if (__leaderLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LEADER))) {
                    return false;
                }
                if (__leaderLoaded && __leaderValue != __other.leader()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PHONE)) != __other.__isVisible(PropId.byIndex(SLOT_PHONE))) {
                    return false;
                }
                boolean __phoneLoaded = this.__phoneLoaded;
                if (__phoneLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PHONE))) {
                    return false;
                }
                if (__phoneLoaded && __phoneValue != __other.phone()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_EMAIL)) != __other.__isVisible(PropId.byIndex(SLOT_EMAIL))) {
                    return false;
                }
                boolean __emailLoaded = this.__emailLoaded;
                if (__emailLoaded != __other.__isLoaded(PropId.byIndex(SLOT_EMAIL))) {
                    return false;
                }
                if (__emailLoaded && __emailValue != __other.email()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_STATUS)) != __other.__isVisible(PropId.byIndex(SLOT_STATUS))) {
                    return false;
                }
                boolean __statusLoaded = this.__statusLoaded;
                if (__statusLoaded != __other.__isLoaded(PropId.byIndex(SLOT_STATUS))) {
                    return false;
                }
                if (__statusLoaded && __statusValue != __other.status()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ANCESTORS)) != __other.__isVisible(PropId.byIndex(SLOT_ANCESTORS))) {
                    return false;
                }
                boolean __ancestorsLoaded = this.__ancestorsLoaded;
                if (__ancestorsLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ANCESTORS))) {
                    return false;
                }
                if (__ancestorsLoaded && __ancestorsValue != __other.ancestors()) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean __equals(Object obj, boolean shallow) {
                return shallow ? __shallowEquals(obj) : equals(obj);
            }

            @Override
            public String toString() {
                return ImmutableObjects.toString(this);
            }
        }

        @GeneratedBy(
                type = SysDept.class
        )
        private static class DraftImpl extends Implementor implements DraftSpi, SysDeptDraft {
            private DraftContext __ctx;

            private Impl __base;

            private Impl __modified;

            private boolean __resolving;

            private SysDept __resolved;

            DraftImpl(DraftContext ctx, SysDept base) {
                __ctx = ctx;
                if (base != null) {
                    __base = (Impl)base;
                }
                else {
                    __modified = new Impl();
                }
            }

            @Override
            public boolean __isLoaded(PropId prop) {
                return (__modified!= null ? __modified : __base).__isLoaded(prop);
            }

            @Override
            public boolean __isLoaded(String prop) {
                return (__modified!= null ? __modified : __base).__isLoaded(prop);
            }

            @Override
            public boolean __isVisible(PropId prop) {
                return (__modified!= null ? __modified : __base).__isVisible(prop);
            }

            @Override
            public boolean __isVisible(String prop) {
                return (__modified!= null ? __modified : __base).__isVisible(prop);
            }

            @Override
            public int hashCode() {
                return (__modified!= null ? __modified : __base).hashCode();
            }

            @Override
            public int __hashCode(boolean shallow) {
                return (__modified!= null ? __modified : __base).__hashCode(shallow);
            }

            @Override
            public boolean equals(Object obj) {
                return (__modified!= null ? __modified : __base).equals(obj);
            }

            @Override
            public boolean __equals(Object obj, boolean shallow) {
                return (__modified!= null ? __modified : __base).__equals(obj, shallow);
            }

            @Override
            public String toString() {
                return ImmutableObjects.toString(this);
            }

            @Override
            @JsonIgnore
            public long id() {
                return (__modified!= null ? __modified : __base).id();
            }

            @Override
            public SysDeptDraft setId(long id) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__idValue = id;
                __tmpModified.__idLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public LocalDateTime createdTime() {
                return (__modified!= null ? __modified : __base).createdTime();
            }

            @Override
            public SysDeptDraft setCreatedTime(LocalDateTime createdTime) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (createdTime == null) {
                    throw new IllegalArgumentException(
                        "'createdTime' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__createdTimeValue = createdTime;
                return this;
            }

            @Override
            @JsonIgnore
            public LocalDateTime updatedTime() {
                return (__modified!= null ? __modified : __base).updatedTime();
            }

            @Override
            public SysDeptDraft setUpdatedTime(LocalDateTime updatedTime) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (updatedTime == null) {
                    throw new IllegalArgumentException(
                        "'updatedTime' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__updatedTimeValue = updatedTime;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long createdBy() {
                return (__modified!= null ? __modified : __base).createdBy();
            }

            @Override
            public SysDeptDraft setCreatedBy(Long createdBy) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__createdByValue = createdBy;
                __tmpModified.__createdByLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long updatedBy() {
                return (__modified!= null ? __modified : __base).updatedBy();
            }

            @Override
            public SysDeptDraft setUpdatedBy(Long updatedBy) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__updatedByValue = updatedBy;
                __tmpModified.__updatedByLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long parentId() {
                SysDept __target = parent();
                return __target != null ? __target.id() : null;
            }

            @Override
            public SysDeptDraft setParentId(Long parentId) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (parentId != null) {
                    setParent(ImmutableObjects.makeIdOnly(SysDept.class, parentId));
                } else {
                    setParent(null);
                }
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Integer sort() {
                return (__modified!= null ? __modified : __base).sort();
            }

            @Override
            public SysDeptDraft setSort(Integer sort) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__sortValue = sort;
                __tmpModified.__sortLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public SysDeptDraft parent() {
                return __ctx.toDraftObject((__modified!= null ? __modified : __base).parent());
            }

            @Override
            public SysDeptDraft parent(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_PARENT)) || parent() == null)) {
                    setParent(SysDeptDraft.$.produce(null, null));
                }
                return __ctx.toDraftObject((__modified!= null ? __modified : __base).parent());
            }

            @Override
            public SysDeptDraft setParent(SysDept parent) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__parentValue = parent;
                __tmpModified.__parentLoaded = true;
                return this;
            }

            @Override
            public SysDeptDraft applyParent(DraftConsumer<SysDeptDraft> block) {
                applyParent(null, block);
                return this;
            }

            @Override
            public SysDeptDraft applyParent(SysDept base, DraftConsumer<SysDeptDraft> block) {
                setParent(SysDeptDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            public List<SysDept> children() {
                return __ctx.toDraftList((__modified!= null ? __modified : __base).children(), SysDept.class, true);
            }

            @Override
            public List<SysDeptDraft> children(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_CHILDREN)))) {
                    setChildren(new ArrayList<>());
                }
                return __ctx.toDraftList((__modified!= null ? __modified : __base).children(), SysDept.class, true);
            }

            @Override
            public SysDeptDraft setChildren(List<SysDept> children) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (children == null) {
                    throw new IllegalArgumentException(
                        "'children' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__childrenValue = NonSharedList.of(__tmpModified.__childrenValue, children);
                return this;
            }

            @Override
            public SysDeptDraft addIntoChildren(DraftConsumer<SysDeptDraft> block) {
                addIntoChildren(null, block);
                return this;
            }

            @Override
            public SysDeptDraft addIntoChildren(SysDept base, DraftConsumer<SysDeptDraft> block) {
                children(true).add((SysDeptDraft)SysDeptDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            public String deptName() {
                return (__modified!= null ? __modified : __base).deptName();
            }

            @Override
            public SysDeptDraft setDeptName(String deptName) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (deptName == null) {
                    throw new IllegalArgumentException(
                        "'deptName' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__deptNameValue = deptName;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String leader() {
                return (__modified!= null ? __modified : __base).leader();
            }

            @Override
            public SysDeptDraft setLeader(String leader) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__leaderValue = leader;
                __tmpModified.__leaderLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String phone() {
                return (__modified!= null ? __modified : __base).phone();
            }

            @Override
            public SysDeptDraft setPhone(String phone) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__phoneValue = phone;
                __tmpModified.__phoneLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String email() {
                return (__modified!= null ? __modified : __base).email();
            }

            @Override
            public SysDeptDraft setEmail(String email) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__emailValue = email;
                __tmpModified.__emailLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public int status() {
                return (__modified!= null ? __modified : __base).status();
            }

            @Override
            public SysDeptDraft setStatus(int status) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__statusValue = status;
                __tmpModified.__statusLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String ancestors() {
                return (__modified!= null ? __modified : __base).ancestors();
            }

            @Override
            public SysDeptDraft setAncestors(String ancestors) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__ancestorsValue = ancestors;
                __tmpModified.__ancestorsLoaded = true;
                return this;
            }

            @SuppressWarnings("all")
            @Override
            public void __set(PropId prop, Object value) {
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		__set(prop.asName(), value);
                    return;
                    case SLOT_ID:
                    		if (value == null) throw new IllegalArgumentException("'id' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setId((Long)value);
                            break;
                    case SLOT_CREATED_TIME:
                    		setCreatedTime((LocalDateTime)value);break;
                    case SLOT_UPDATED_TIME:
                    		setUpdatedTime((LocalDateTime)value);break;
                    case SLOT_CREATED_BY:
                    		setCreatedBy((Long)value);break;
                    case SLOT_UPDATED_BY:
                    		setUpdatedBy((Long)value);break;
                    case SLOT_PARENT_ID:
                    		setParentId((Long)value);break;
                    case SLOT_SORT:
                    		setSort((Integer)value);break;
                    case SLOT_PARENT:
                    		setParent((SysDept)value);break;
                    case SLOT_CHILDREN:
                    		setChildren((List<SysDept>)value);break;
                    case SLOT_DEPT_NAME:
                    		setDeptName((String)value);break;
                    case SLOT_LEADER:
                    		setLeader((String)value);break;
                    case SLOT_PHONE:
                    		setPhone((String)value);break;
                    case SLOT_EMAIL:
                    		setEmail((String)value);break;
                    case SLOT_STATUS:
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case SLOT_ANCESTORS:
                    		setAncestors((String)value);break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + prop + "\"");
                }
            }

            @SuppressWarnings("all")
            @Override
            public void __set(String prop, Object value) {
                switch (prop) {
                    case "id":
                    		if (value == null) throw new IllegalArgumentException("'id' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setId((Long)value);
                            break;
                    case "createdTime":
                    		setCreatedTime((LocalDateTime)value);break;
                    case "updatedTime":
                    		setUpdatedTime((LocalDateTime)value);break;
                    case "createdBy":
                    		setCreatedBy((Long)value);break;
                    case "updatedBy":
                    		setUpdatedBy((Long)value);break;
                    case "parentId":
                    		setParentId((Long)value);break;
                    case "sort":
                    		setSort((Integer)value);break;
                    case "parent":
                    		setParent((SysDept)value);break;
                    case "children":
                    		setChildren((List<SysDept>)value);break;
                    case "deptName":
                    		setDeptName((String)value);break;
                    case "leader":
                    		setLeader((String)value);break;
                    case "phone":
                    		setPhone((String)value);break;
                    case "email":
                    		setEmail((String)value);break;
                    case "status":
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case "ancestors":
                    		setAncestors((String)value);break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + prop + "\"");
                }
            }

            @Override
            public void __show(PropId prop, boolean visible) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Visibility __visibility = (__modified!= null ? __modified : __base).__visibility;
                if (__visibility == null) {
                    if (visible) {
                        return;
                    }
                    __modified().__visibility = __visibility = Visibility.of(15);
                }
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		__show(prop.asName(), visible);
                    return;
                    case SLOT_ID:
                    		__visibility.show(SLOT_ID, visible);break;
                    case SLOT_CREATED_TIME:
                    		__visibility.show(SLOT_CREATED_TIME, visible);break;
                    case SLOT_UPDATED_TIME:
                    		__visibility.show(SLOT_UPDATED_TIME, visible);break;
                    case SLOT_CREATED_BY:
                    		__visibility.show(SLOT_CREATED_BY, visible);break;
                    case SLOT_UPDATED_BY:
                    		__visibility.show(SLOT_UPDATED_BY, visible);break;
                    case SLOT_PARENT_ID:
                    		__visibility.show(SLOT_PARENT_ID, visible);break;
                    case SLOT_SORT:
                    		__visibility.show(SLOT_SORT, visible);break;
                    case SLOT_PARENT:
                    		__visibility.show(SLOT_PARENT, visible);break;
                    case SLOT_CHILDREN:
                    		__visibility.show(SLOT_CHILDREN, visible);break;
                    case SLOT_DEPT_NAME:
                    		__visibility.show(SLOT_DEPT_NAME, visible);break;
                    case SLOT_LEADER:
                    		__visibility.show(SLOT_LEADER, visible);break;
                    case SLOT_PHONE:
                    		__visibility.show(SLOT_PHONE, visible);break;
                    case SLOT_EMAIL:
                    		__visibility.show(SLOT_EMAIL, visible);break;
                    case SLOT_STATUS:
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case SLOT_ANCESTORS:
                    		__visibility.show(SLOT_ANCESTORS, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + 
                                prop + 
                                "\",it does not exists"
                            );
                }
            }

            @Override
            public void __show(String prop, boolean visible) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Visibility __visibility = (__modified!= null ? __modified : __base).__visibility;
                if (__visibility == null) {
                    if (visible) {
                        return;
                    }
                    __modified().__visibility = __visibility = Visibility.of(15);
                }
                switch (prop) {
                    case "id":
                    		__visibility.show(SLOT_ID, visible);break;
                    case "createdTime":
                    		__visibility.show(SLOT_CREATED_TIME, visible);break;
                    case "updatedTime":
                    		__visibility.show(SLOT_UPDATED_TIME, visible);break;
                    case "createdBy":
                    		__visibility.show(SLOT_CREATED_BY, visible);break;
                    case "updatedBy":
                    		__visibility.show(SLOT_UPDATED_BY, visible);break;
                    case "parentId":
                    		__visibility.show(SLOT_PARENT_ID, visible);break;
                    case "sort":
                    		__visibility.show(SLOT_SORT, visible);break;
                    case "parent":
                    		__visibility.show(SLOT_PARENT, visible);break;
                    case "children":
                    		__visibility.show(SLOT_CHILDREN, visible);break;
                    case "deptName":
                    		__visibility.show(SLOT_DEPT_NAME, visible);break;
                    case "leader":
                    		__visibility.show(SLOT_LEADER, visible);break;
                    case "phone":
                    		__visibility.show(SLOT_PHONE, visible);break;
                    case "email":
                    		__visibility.show(SLOT_EMAIL, visible);break;
                    case "status":
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case "ancestors":
                    		__visibility.show(SLOT_ANCESTORS, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + 
                                prop + 
                                "\",it does not exists"
                            );
                }
            }

            @Override
            public void __unload(PropId prop) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		__unload(prop.asName());
                    return;
                    case SLOT_ID:
                    		__modified().__idValue = 0;
                    __modified().__idLoaded = false;break;
                    case SLOT_CREATED_TIME:
                    		__modified().__createdTimeValue = null;break;
                    case SLOT_UPDATED_TIME:
                    		__modified().__updatedTimeValue = null;break;
                    case SLOT_CREATED_BY:
                    		__modified().__createdByValue = null;
                    __modified().__createdByLoaded = false;break;
                    case SLOT_UPDATED_BY:
                    		__modified().__updatedByValue = null;
                    __modified().__updatedByLoaded = false;break;
                    case SLOT_PARENT_ID:
                    		__unload(PropId.byIndex(SLOT_PARENT));break;
                    case SLOT_SORT:
                    		__modified().__sortValue = null;
                    __modified().__sortLoaded = false;break;
                    case SLOT_PARENT:
                    		__modified().__parentValue = null;
                    __modified().__parentLoaded = false;break;
                    case SLOT_CHILDREN:
                    		__modified().__childrenValue = null;break;
                    case SLOT_DEPT_NAME:
                    		__modified().__deptNameValue = null;break;
                    case SLOT_LEADER:
                    		__modified().__leaderValue = null;
                    __modified().__leaderLoaded = false;break;
                    case SLOT_PHONE:
                    		__modified().__phoneValue = null;
                    __modified().__phoneLoaded = false;break;
                    case SLOT_EMAIL:
                    		__modified().__emailValue = null;
                    __modified().__emailLoaded = false;break;
                    case SLOT_STATUS:
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case SLOT_ANCESTORS:
                    		__modified().__ancestorsValue = null;
                    __modified().__ancestorsLoaded = false;break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
                }
            }

            @Override
            public void __unload(String prop) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                switch (prop) {
                    case "id":
                    		__modified().__idValue = 0;
                    __modified().__idLoaded = false;break;
                    case "createdTime":
                    		__modified().__createdTimeValue = null;break;
                    case "updatedTime":
                    		__modified().__updatedTimeValue = null;break;
                    case "createdBy":
                    		__modified().__createdByValue = null;
                    __modified().__createdByLoaded = false;break;
                    case "updatedBy":
                    		__modified().__updatedByValue = null;
                    __modified().__updatedByLoaded = false;break;
                    case "parentId":
                    		__unload(PropId.byIndex(SLOT_PARENT));break;
                    case "sort":
                    		__modified().__sortValue = null;
                    __modified().__sortLoaded = false;break;
                    case "parent":
                    		__modified().__parentValue = null;
                    __modified().__parentLoaded = false;break;
                    case "children":
                    		__modified().__childrenValue = null;break;
                    case "deptName":
                    		__modified().__deptNameValue = null;break;
                    case "leader":
                    		__modified().__leaderValue = null;
                    __modified().__leaderLoaded = false;break;
                    case "phone":
                    		__modified().__phoneValue = null;
                    __modified().__phoneLoaded = false;break;
                    case "email":
                    		__modified().__emailValue = null;
                    __modified().__emailLoaded = false;break;
                    case "status":
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case "ancestors":
                    		__modified().__ancestorsValue = null;
                    __modified().__ancestorsLoaded = false;break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDept\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
                }
            }

            @Override
            public DraftContext __draftContext() {
                return __ctx;
            }

            @Override
            public Object __resolve() {
                if (__resolved != null) {
                    return __resolved;
                }
                if (__resolving) {
                    throw new CircularReferenceException();
                }
                __resolving = true;
                try {
                    Implementor base = __base;
                    Impl __tmpModified = __modified;
                    if (__tmpModified == null) {
                        if (base.__isLoaded(PropId.byIndex(SLOT_PARENT))) {
                            SysDept oldValue = base.parent();
                            SysDept newValue = __ctx.resolveObject(oldValue);
                            if (oldValue != newValue) {
                                setParent(newValue);
                            }
                        }
                        if (base.__isLoaded(PropId.byIndex(SLOT_CHILDREN))) {
                            List<SysDept> oldValue = base.children();
                            List<SysDept> newValue = __ctx.resolveList(oldValue);
                            if (oldValue != newValue) {
                                setChildren(newValue);
                            }
                        }
                        __tmpModified = __modified;
                    }
                    else {
                        __tmpModified.__parentValue = __ctx.resolveObject(__tmpModified.__parentValue);
                        __tmpModified.__childrenValue = NonSharedList.of(__tmpModified.__childrenValue, __ctx.resolveList(__tmpModified.__childrenValue));
                    }
                    if (__base != null && __tmpModified == null) {
                        this.__resolved = base;
                        return base;
                    }
                    this.__resolved = __tmpModified;
                    return __tmpModified;
                }
                finally {
                    __resolving = false;
                }
            }

            @Override
            public boolean __isResolved() {
                return __resolved != null;
            }

            Impl __modified() {
                Impl __tmpModified = __modified;
                if (__tmpModified == null) {
                    __tmpModified = __base.clone();
                    __modified = __tmpModified;
                }
                return __tmpModified;
            }
        }
    }

    @GeneratedBy(
            type = SysDept.class
    )
    class Builder {
        private final Producer.DraftImpl __draft;

        public Builder() {
            this(null);
        }

        public Builder(@Nullable SysDept base) {
            __draft = new Producer.DraftImpl(null, base);
            __draft.__show(PropId.byIndex(Producer.SLOT_PARENT_ID), false);
            __draft.__show(PropId.byIndex(Producer.SLOT_PARENT), false);
        }

        public Builder id(@NotNull Long id) {
            if (id != null) {
                __draft.setId(id);
            }
            return this;
        }

        public Builder createdTime(@NotNull LocalDateTime createdTime) {
            if (createdTime != null) {
                __draft.setCreatedTime(createdTime);
            }
            return this;
        }

        public Builder updatedTime(@NotNull LocalDateTime updatedTime) {
            if (updatedTime != null) {
                __draft.setUpdatedTime(updatedTime);
            }
            return this;
        }

        public Builder createdBy(@Nullable Long createdBy) {
            __draft.setCreatedBy(createdBy);
            return this;
        }

        public Builder updatedBy(@Nullable Long updatedBy) {
            __draft.setUpdatedBy(updatedBy);
            return this;
        }

        public Builder parentId(@Nullable Long parentId) {
            __draft.setParentId(parentId);
            __draft.__show(PropId.byIndex(Producer.SLOT_PARENT_ID), true);
            return this;
        }

        public Builder sort(@Nullable Integer sort) {
            __draft.setSort(sort);
            return this;
        }

        public Builder parent(@Nullable SysDept parent) {
            __draft.setParent(parent);
            __draft.__show(PropId.byIndex(Producer.SLOT_PARENT), true);
            return this;
        }

        public Builder children(@NotNull List<SysDept> children) {
            if (children != null) {
                __draft.setChildren(children);
            }
            return this;
        }

        public Builder deptName(@NotNull String deptName) {
            if (deptName != null) {
                __draft.setDeptName(deptName);
            }
            return this;
        }

        public Builder leader(@Nullable String leader) {
            __draft.setLeader(leader);
            return this;
        }

        public Builder phone(@Nullable String phone) {
            __draft.setPhone(phone);
            return this;
        }

        public Builder email(@Nullable String email) {
            __draft.setEmail(email);
            return this;
        }

        public Builder status(@NotNull Integer status) {
            if (status != null) {
                __draft.setStatus(status);
            }
            return this;
        }

        public Builder ancestors(@Nullable String ancestors) {
            __draft.setAncestors(ancestors);
            return this;
        }

        public SysDept build() {
            return (SysDept)__draft.__modified();
        }
    }
}
