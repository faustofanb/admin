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
import org.babyfish.jimmer.sql.ManyToMany;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@GeneratedBy(
        type = SysRole.class
)
public interface SysRoleDraft extends SysRole, BaseEntityDraft {
    SysRoleDraft.Producer $ = Producer.INSTANCE;

    @OldChain
    SysRoleDraft setId(long id);

    @OldChain
    SysRoleDraft setCreatedTime(LocalDateTime createdTime);

    @OldChain
    SysRoleDraft setUpdatedTime(LocalDateTime updatedTime);

    @OldChain
    SysRoleDraft setCreatedBy(Long createdBy);

    @OldChain
    SysRoleDraft setUpdatedBy(Long updatedBy);

    @OldChain
    SysRoleDraft setRoleName(String roleName);

    @OldChain
    SysRoleDraft setRoleKey(String roleKey);

    @OldChain
    SysRoleDraft setRoleSort(int roleSort);

    @OldChain
    SysRoleDraft setDataScope(String dataScope);

    @OldChain
    SysRoleDraft setStatus(int status);

    @OldChain
    SysRoleDraft setRemark(String remark);

    List<SysMenuDraft> menus(boolean autoCreate);

    @OldChain
    SysRoleDraft setMenus(List<SysMenu> menus);

    @OldChain
    SysRoleDraft addIntoMenus(DraftConsumer<SysMenuDraft> block);

    @OldChain
    SysRoleDraft addIntoMenus(SysMenu base, DraftConsumer<SysMenuDraft> block);

    List<SysDeptDraft> depts(boolean autoCreate);

    @OldChain
    SysRoleDraft setDepts(List<SysDept> depts);

    @OldChain
    SysRoleDraft addIntoDepts(DraftConsumer<SysDeptDraft> block);

    @OldChain
    SysRoleDraft addIntoDepts(SysDept base, DraftConsumer<SysDeptDraft> block);

    List<SysUserDraft> users(boolean autoCreate);

    @OldChain
    SysRoleDraft setUsers(List<SysUser> users);

    @OldChain
    SysRoleDraft addIntoUsers(DraftConsumer<SysUserDraft> block);

    @OldChain
    SysRoleDraft addIntoUsers(SysUser base, DraftConsumer<SysUserDraft> block);

    @GeneratedBy(
            type = SysRole.class
    )
    class Producer {
        static final Producer INSTANCE = new Producer();

        public static final int SLOT_ID = 0;

        public static final int SLOT_CREATED_TIME = 1;

        public static final int SLOT_UPDATED_TIME = 2;

        public static final int SLOT_CREATED_BY = 3;

        public static final int SLOT_UPDATED_BY = 4;

        public static final int SLOT_ROLE_NAME = 5;

        public static final int SLOT_ROLE_KEY = 6;

        public static final int SLOT_ROLE_SORT = 7;

        public static final int SLOT_DATA_SCOPE = 8;

        public static final int SLOT_STATUS = 9;

        public static final int SLOT_REMARK = 10;

        public static final int SLOT_MENUS = 11;

        public static final int SLOT_DEPTS = 12;

        public static final int SLOT_USERS = 13;

        public static final ImmutableType TYPE = ImmutableType
            .newBuilder(
                "0.9.96",
                SysRole.class,
                Collections.singleton(BaseEntityDraft.Producer.TYPE),
                (ctx, base) -> new DraftImpl(ctx, (SysRole)base)
            )
            .redefine("id", SLOT_ID)
            .redefine("createdTime", SLOT_CREATED_TIME)
            .redefine("updatedTime", SLOT_UPDATED_TIME)
            .redefine("createdBy", SLOT_CREATED_BY)
            .redefine("updatedBy", SLOT_UPDATED_BY)
            .add(SLOT_ROLE_NAME, "roleName", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_ROLE_KEY, "roleKey", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_ROLE_SORT, "roleSort", ImmutablePropCategory.SCALAR, int.class, false)
            .add(SLOT_DATA_SCOPE, "dataScope", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_STATUS, "status", ImmutablePropCategory.SCALAR, int.class, false)
            .add(SLOT_REMARK, "remark", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_MENUS, "menus", ManyToMany.class, SysMenu.class, false)
            .add(SLOT_DEPTS, "depts", ManyToMany.class, SysDept.class, false)
            .add(SLOT_USERS, "users", ManyToMany.class, SysUser.class, false)
            .build();

        private Producer() {
        }

        public SysRole produce(DraftConsumer<SysRoleDraft> block) {
            return (SysRole)Internal.produce(TYPE, null, block);
        }

        public SysRole produce(SysRole base, DraftConsumer<SysRoleDraft> block) {
            return (SysRole)Internal.produce(TYPE, base, block);
        }

        public SysRole produce(boolean resolveImmediately, DraftConsumer<SysRoleDraft> block) {
            return (SysRole)Internal.produce(TYPE, null, resolveImmediately, block);
        }

        public SysRole produce(SysRole base, boolean resolveImmediately,
                DraftConsumer<SysRoleDraft> block) {
            return (SysRole)Internal.produce(TYPE, base, resolveImmediately, block);
        }

        /**
         * Class, not interface, for free-marker
         */
        @GeneratedBy(
                type = SysRole.class
        )
        @JsonPropertyOrder({"dummyPropForJacksonError__", "id", "createdTime", "updatedTime", "createdBy", "updatedBy", "roleName", "roleKey", "roleSort", "dataScope", "status", "remark", "menus", "depts", "users"})
        public abstract static class Implementor implements SysRole, ImmutableSpi {
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
                    case SLOT_ROLE_NAME:
                    		return roleName();
                    case SLOT_ROLE_KEY:
                    		return roleKey();
                    case SLOT_ROLE_SORT:
                    		return (Integer)roleSort();
                    case SLOT_DATA_SCOPE:
                    		return dataScope();
                    case SLOT_STATUS:
                    		return (Integer)status();
                    case SLOT_REMARK:
                    		return remark();
                    case SLOT_MENUS:
                    		return menus();
                    case SLOT_DEPTS:
                    		return depts();
                    case SLOT_USERS:
                    		return users();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + prop + "\"");
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
                    case "roleName":
                    		return roleName();
                    case "roleKey":
                    		return roleKey();
                    case "roleSort":
                    		return (Integer)roleSort();
                    case "dataScope":
                    		return dataScope();
                    case "status":
                    		return (Integer)status();
                    case "remark":
                    		return remark();
                    case "menus":
                    		return menus();
                    case "depts":
                    		return depts();
                    case "users":
                    		return users();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + prop + "\"");
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

            public final String getRoleName() {
                return roleName();
            }

            public final String getRoleKey() {
                return roleKey();
            }

            public final int getRoleSort() {
                return roleSort();
            }

            @Nullable
            public final String getDataScope() {
                return dataScope();
            }

            public final int getStatus() {
                return status();
            }

            @Nullable
            public final String getRemark() {
                return remark();
            }

            public final List<SysMenu> getMenus() {
                return menus();
            }

            public final List<SysDept> getDepts() {
                return depts();
            }

            public final List<SysUser> getUsers() {
                return users();
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
                type = SysRole.class
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

            String __roleNameValue;

            String __roleKeyValue;

            int __roleSortValue;

            boolean __roleSortLoaded = false;

            String __dataScopeValue;

            boolean __dataScopeLoaded = false;

            int __statusValue;

            boolean __statusLoaded = false;

            String __remarkValue;

            boolean __remarkLoaded = false;

            NonSharedList<SysMenu> __menusValue;

            NonSharedList<SysDept> __deptsValue;

            NonSharedList<SysUser> __usersValue;

            @Override
            @JsonIgnore
            public long id() {
                if (!__idLoaded) {
                    throw new UnloadedException(SysRole.class, "id");
                }
                return __idValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime createdTime() {
                if (__createdTimeValue == null) {
                    throw new UnloadedException(SysRole.class, "createdTime");
                }
                return __createdTimeValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime updatedTime() {
                if (__updatedTimeValue == null) {
                    throw new UnloadedException(SysRole.class, "updatedTime");
                }
                return __updatedTimeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long createdBy() {
                if (!__createdByLoaded) {
                    throw new UnloadedException(SysRole.class, "createdBy");
                }
                return __createdByValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long updatedBy() {
                if (!__updatedByLoaded) {
                    throw new UnloadedException(SysRole.class, "updatedBy");
                }
                return __updatedByValue;
            }

            @Override
            @JsonIgnore
            @Description("角色名称")
            public String roleName() {
                if (__roleNameValue == null) {
                    throw new UnloadedException(SysRole.class, "roleName");
                }
                return __roleNameValue;
            }

            @Override
            @JsonIgnore
            @Description("角色权限字符串")
            public String roleKey() {
                if (__roleKeyValue == null) {
                    throw new UnloadedException(SysRole.class, "roleKey");
                }
                return __roleKeyValue;
            }

            @Override
            @JsonIgnore
            @Description("显示顺序")
            public int roleSort() {
                if (!__roleSortLoaded) {
                    throw new UnloadedException(SysRole.class, "roleSort");
                }
                return __roleSortValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
            public String dataScope() {
                if (!__dataScopeLoaded) {
                    throw new UnloadedException(SysRole.class, "dataScope");
                }
                return __dataScopeValue;
            }

            @Override
            @JsonIgnore
            @Description("角色状态（0正常 1停用）")
            public int status() {
                if (!__statusLoaded) {
                    throw new UnloadedException(SysRole.class, "status");
                }
                return __statusValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("备注")
            public String remark() {
                if (!__remarkLoaded) {
                    throw new UnloadedException(SysRole.class, "remark");
                }
                return __remarkValue;
            }

            @Override
            @JsonIgnore
            @Description("菜单组")
            public List<SysMenu> menus() {
                if (__menusValue == null) {
                    throw new UnloadedException(SysRole.class, "menus");
                }
                return __menusValue;
            }

            @Override
            @JsonIgnore
            @Description("部门组（数据权限）")
            public List<SysDept> depts() {
                if (__deptsValue == null) {
                    throw new UnloadedException(SysRole.class, "depts");
                }
                return __deptsValue;
            }

            @Override
            @JsonIgnore
            public List<SysUser> users() {
                if (__usersValue == null) {
                    throw new UnloadedException(SysRole.class, "users");
                }
                return __usersValue;
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
                    case SLOT_ROLE_NAME:
                    		return __roleNameValue != null;
                    case SLOT_ROLE_KEY:
                    		return __roleKeyValue != null;
                    case SLOT_ROLE_SORT:
                    		return __roleSortLoaded;
                    case SLOT_DATA_SCOPE:
                    		return __dataScopeLoaded;
                    case SLOT_STATUS:
                    		return __statusLoaded;
                    case SLOT_REMARK:
                    		return __remarkLoaded;
                    case SLOT_MENUS:
                    		return __menusValue != null;
                    case SLOT_DEPTS:
                    		return __deptsValue != null;
                    case SLOT_USERS:
                    		return __usersValue != null;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + prop + "\"");
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
                    case "roleName":
                    		return __roleNameValue != null;
                    case "roleKey":
                    		return __roleKeyValue != null;
                    case "roleSort":
                    		return __roleSortLoaded;
                    case "dataScope":
                    		return __dataScopeLoaded;
                    case "status":
                    		return __statusLoaded;
                    case "remark":
                    		return __remarkLoaded;
                    case "menus":
                    		return __menusValue != null;
                    case "depts":
                    		return __deptsValue != null;
                    case "users":
                    		return __usersValue != null;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + prop + "\"");
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
                    case SLOT_ROLE_NAME:
                    		return __visibility.visible(SLOT_ROLE_NAME);
                    case SLOT_ROLE_KEY:
                    		return __visibility.visible(SLOT_ROLE_KEY);
                    case SLOT_ROLE_SORT:
                    		return __visibility.visible(SLOT_ROLE_SORT);
                    case SLOT_DATA_SCOPE:
                    		return __visibility.visible(SLOT_DATA_SCOPE);
                    case SLOT_STATUS:
                    		return __visibility.visible(SLOT_STATUS);
                    case SLOT_REMARK:
                    		return __visibility.visible(SLOT_REMARK);
                    case SLOT_MENUS:
                    		return __visibility.visible(SLOT_MENUS);
                    case SLOT_DEPTS:
                    		return __visibility.visible(SLOT_DEPTS);
                    case SLOT_USERS:
                    		return __visibility.visible(SLOT_USERS);
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
                    case "roleName":
                    		return __visibility.visible(SLOT_ROLE_NAME);
                    case "roleKey":
                    		return __visibility.visible(SLOT_ROLE_KEY);
                    case "roleSort":
                    		return __visibility.visible(SLOT_ROLE_SORT);
                    case "dataScope":
                    		return __visibility.visible(SLOT_DATA_SCOPE);
                    case "status":
                    		return __visibility.visible(SLOT_STATUS);
                    case "remark":
                    		return __visibility.visible(SLOT_REMARK);
                    case "menus":
                    		return __visibility.visible(SLOT_MENUS);
                    case "depts":
                    		return __visibility.visible(SLOT_DEPTS);
                    case "users":
                    		return __visibility.visible(SLOT_USERS);
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
                if (__roleNameValue != null) {
                    hash = 31 * hash + __roleNameValue.hashCode();
                }
                if (__roleKeyValue != null) {
                    hash = 31 * hash + __roleKeyValue.hashCode();
                }
                if (__roleSortLoaded) {
                    hash = 31 * hash + Integer.hashCode(__roleSortValue);
                }
                if (__dataScopeLoaded && __dataScopeValue != null) {
                    hash = 31 * hash + __dataScopeValue.hashCode();
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__remarkLoaded && __remarkValue != null) {
                    hash = 31 * hash + __remarkValue.hashCode();
                }
                if (__menusValue != null) {
                    hash = 31 * hash + __menusValue.hashCode();
                }
                if (__deptsValue != null) {
                    hash = 31 * hash + __deptsValue.hashCode();
                }
                if (__usersValue != null) {
                    hash = 31 * hash + __usersValue.hashCode();
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
                if (__roleNameValue != null) {
                    hash = 31 * hash + System.identityHashCode(__roleNameValue);
                }
                if (__roleKeyValue != null) {
                    hash = 31 * hash + System.identityHashCode(__roleKeyValue);
                }
                if (__roleSortLoaded) {
                    hash = 31 * hash + Integer.hashCode(__roleSortValue);
                }
                if (__dataScopeLoaded) {
                    hash = 31 * hash + System.identityHashCode(__dataScopeValue);
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__remarkLoaded) {
                    hash = 31 * hash + System.identityHashCode(__remarkValue);
                }
                if (__menusValue != null) {
                    hash = 31 * hash + System.identityHashCode(__menusValue);
                }
                if (__deptsValue != null) {
                    hash = 31 * hash + System.identityHashCode(__deptsValue);
                }
                if (__usersValue != null) {
                    hash = 31 * hash + System.identityHashCode(__usersValue);
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
                if (__isVisible(PropId.byIndex(SLOT_ROLE_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_ROLE_NAME))) {
                    return false;
                }
                boolean __roleNameLoaded = __roleNameValue != null;
                if (__roleNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLE_NAME))) {
                    return false;
                }
                if (__roleNameLoaded && !Objects.equals(__roleNameValue, __other.roleName())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ROLE_KEY)) != __other.__isVisible(PropId.byIndex(SLOT_ROLE_KEY))) {
                    return false;
                }
                boolean __roleKeyLoaded = __roleKeyValue != null;
                if (__roleKeyLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLE_KEY))) {
                    return false;
                }
                if (__roleKeyLoaded && !Objects.equals(__roleKeyValue, __other.roleKey())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ROLE_SORT)) != __other.__isVisible(PropId.byIndex(SLOT_ROLE_SORT))) {
                    return false;
                }
                boolean __roleSortLoaded = this.__roleSortLoaded;
                if (__roleSortLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLE_SORT))) {
                    return false;
                }
                if (__roleSortLoaded && __roleSortValue != __other.roleSort()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DATA_SCOPE)) != __other.__isVisible(PropId.byIndex(SLOT_DATA_SCOPE))) {
                    return false;
                }
                boolean __dataScopeLoaded = this.__dataScopeLoaded;
                if (__dataScopeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DATA_SCOPE))) {
                    return false;
                }
                if (__dataScopeLoaded && !Objects.equals(__dataScopeValue, __other.dataScope())) {
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
                if (__isVisible(PropId.byIndex(SLOT_REMARK)) != __other.__isVisible(PropId.byIndex(SLOT_REMARK))) {
                    return false;
                }
                boolean __remarkLoaded = this.__remarkLoaded;
                if (__remarkLoaded != __other.__isLoaded(PropId.byIndex(SLOT_REMARK))) {
                    return false;
                }
                if (__remarkLoaded && !Objects.equals(__remarkValue, __other.remark())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_MENUS)) != __other.__isVisible(PropId.byIndex(SLOT_MENUS))) {
                    return false;
                }
                boolean __menusLoaded = __menusValue != null;
                if (__menusLoaded != __other.__isLoaded(PropId.byIndex(SLOT_MENUS))) {
                    return false;
                }
                if (__menusLoaded && !Objects.equals(__menusValue, __other.menus())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DEPTS)) != __other.__isVisible(PropId.byIndex(SLOT_DEPTS))) {
                    return false;
                }
                boolean __deptsLoaded = __deptsValue != null;
                if (__deptsLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DEPTS))) {
                    return false;
                }
                if (__deptsLoaded && !Objects.equals(__deptsValue, __other.depts())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_USERS)) != __other.__isVisible(PropId.byIndex(SLOT_USERS))) {
                    return false;
                }
                boolean __usersLoaded = __usersValue != null;
                if (__usersLoaded != __other.__isLoaded(PropId.byIndex(SLOT_USERS))) {
                    return false;
                }
                if (__usersLoaded && !Objects.equals(__usersValue, __other.users())) {
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
                if (__isVisible(PropId.byIndex(SLOT_ROLE_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_ROLE_NAME))) {
                    return false;
                }
                boolean __roleNameLoaded = __roleNameValue != null;
                if (__roleNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLE_NAME))) {
                    return false;
                }
                if (__roleNameLoaded && __roleNameValue != __other.roleName()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ROLE_KEY)) != __other.__isVisible(PropId.byIndex(SLOT_ROLE_KEY))) {
                    return false;
                }
                boolean __roleKeyLoaded = __roleKeyValue != null;
                if (__roleKeyLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLE_KEY))) {
                    return false;
                }
                if (__roleKeyLoaded && __roleKeyValue != __other.roleKey()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ROLE_SORT)) != __other.__isVisible(PropId.byIndex(SLOT_ROLE_SORT))) {
                    return false;
                }
                boolean __roleSortLoaded = this.__roleSortLoaded;
                if (__roleSortLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLE_SORT))) {
                    return false;
                }
                if (__roleSortLoaded && __roleSortValue != __other.roleSort()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DATA_SCOPE)) != __other.__isVisible(PropId.byIndex(SLOT_DATA_SCOPE))) {
                    return false;
                }
                boolean __dataScopeLoaded = this.__dataScopeLoaded;
                if (__dataScopeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DATA_SCOPE))) {
                    return false;
                }
                if (__dataScopeLoaded && __dataScopeValue != __other.dataScope()) {
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
                if (__isVisible(PropId.byIndex(SLOT_REMARK)) != __other.__isVisible(PropId.byIndex(SLOT_REMARK))) {
                    return false;
                }
                boolean __remarkLoaded = this.__remarkLoaded;
                if (__remarkLoaded != __other.__isLoaded(PropId.byIndex(SLOT_REMARK))) {
                    return false;
                }
                if (__remarkLoaded && __remarkValue != __other.remark()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_MENUS)) != __other.__isVisible(PropId.byIndex(SLOT_MENUS))) {
                    return false;
                }
                boolean __menusLoaded = __menusValue != null;
                if (__menusLoaded != __other.__isLoaded(PropId.byIndex(SLOT_MENUS))) {
                    return false;
                }
                if (__menusLoaded && __menusValue != __other.menus()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DEPTS)) != __other.__isVisible(PropId.byIndex(SLOT_DEPTS))) {
                    return false;
                }
                boolean __deptsLoaded = __deptsValue != null;
                if (__deptsLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DEPTS))) {
                    return false;
                }
                if (__deptsLoaded && __deptsValue != __other.depts()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_USERS)) != __other.__isVisible(PropId.byIndex(SLOT_USERS))) {
                    return false;
                }
                boolean __usersLoaded = __usersValue != null;
                if (__usersLoaded != __other.__isLoaded(PropId.byIndex(SLOT_USERS))) {
                    return false;
                }
                if (__usersLoaded && __usersValue != __other.users()) {
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
                type = SysRole.class
        )
        private static class DraftImpl extends Implementor implements DraftSpi, SysRoleDraft {
            private DraftContext __ctx;

            private Impl __base;

            private Impl __modified;

            private boolean __resolving;

            private SysRole __resolved;

            DraftImpl(DraftContext ctx, SysRole base) {
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
            public SysRoleDraft setId(long id) {
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
            public SysRoleDraft setCreatedTime(LocalDateTime createdTime) {
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
            public SysRoleDraft setUpdatedTime(LocalDateTime updatedTime) {
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
            public SysRoleDraft setCreatedBy(Long createdBy) {
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
            public SysRoleDraft setUpdatedBy(Long updatedBy) {
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
            public String roleName() {
                return (__modified!= null ? __modified : __base).roleName();
            }

            @Override
            public SysRoleDraft setRoleName(String roleName) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (roleName == null) {
                    throw new IllegalArgumentException(
                        "'roleName' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__roleNameValue = roleName;
                return this;
            }

            @Override
            @JsonIgnore
            public String roleKey() {
                return (__modified!= null ? __modified : __base).roleKey();
            }

            @Override
            public SysRoleDraft setRoleKey(String roleKey) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (roleKey == null) {
                    throw new IllegalArgumentException(
                        "'roleKey' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__roleKeyValue = roleKey;
                return this;
            }

            @Override
            @JsonIgnore
            public int roleSort() {
                return (__modified!= null ? __modified : __base).roleSort();
            }

            @Override
            public SysRoleDraft setRoleSort(int roleSort) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__roleSortValue = roleSort;
                __tmpModified.__roleSortLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String dataScope() {
                return (__modified!= null ? __modified : __base).dataScope();
            }

            @Override
            public SysRoleDraft setDataScope(String dataScope) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__dataScopeValue = dataScope;
                __tmpModified.__dataScopeLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public int status() {
                return (__modified!= null ? __modified : __base).status();
            }

            @Override
            public SysRoleDraft setStatus(int status) {
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
            public String remark() {
                return (__modified!= null ? __modified : __base).remark();
            }

            @Override
            public SysRoleDraft setRemark(String remark) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__remarkValue = remark;
                __tmpModified.__remarkLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public List<SysMenu> menus() {
                return __ctx.toDraftList((__modified!= null ? __modified : __base).menus(), SysMenu.class, true);
            }

            @Override
            public List<SysMenuDraft> menus(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_MENUS)))) {
                    setMenus(new ArrayList<>());
                }
                return __ctx.toDraftList((__modified!= null ? __modified : __base).menus(), SysMenu.class, true);
            }

            @Override
            public SysRoleDraft setMenus(List<SysMenu> menus) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (menus == null) {
                    throw new IllegalArgumentException(
                        "'menus' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__menusValue = NonSharedList.of(__tmpModified.__menusValue, menus);
                return this;
            }

            @Override
            public SysRoleDraft addIntoMenus(DraftConsumer<SysMenuDraft> block) {
                addIntoMenus(null, block);
                return this;
            }

            @Override
            public SysRoleDraft addIntoMenus(SysMenu base, DraftConsumer<SysMenuDraft> block) {
                menus(true).add((SysMenuDraft)SysMenuDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            public List<SysDept> depts() {
                return __ctx.toDraftList((__modified!= null ? __modified : __base).depts(), SysDept.class, true);
            }

            @Override
            public List<SysDeptDraft> depts(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_DEPTS)))) {
                    setDepts(new ArrayList<>());
                }
                return __ctx.toDraftList((__modified!= null ? __modified : __base).depts(), SysDept.class, true);
            }

            @Override
            public SysRoleDraft setDepts(List<SysDept> depts) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (depts == null) {
                    throw new IllegalArgumentException(
                        "'depts' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__deptsValue = NonSharedList.of(__tmpModified.__deptsValue, depts);
                return this;
            }

            @Override
            public SysRoleDraft addIntoDepts(DraftConsumer<SysDeptDraft> block) {
                addIntoDepts(null, block);
                return this;
            }

            @Override
            public SysRoleDraft addIntoDepts(SysDept base, DraftConsumer<SysDeptDraft> block) {
                depts(true).add((SysDeptDraft)SysDeptDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            public List<SysUser> users() {
                return __ctx.toDraftList((__modified!= null ? __modified : __base).users(), SysUser.class, true);
            }

            @Override
            public List<SysUserDraft> users(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_USERS)))) {
                    setUsers(new ArrayList<>());
                }
                return __ctx.toDraftList((__modified!= null ? __modified : __base).users(), SysUser.class, true);
            }

            @Override
            public SysRoleDraft setUsers(List<SysUser> users) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (users == null) {
                    throw new IllegalArgumentException(
                        "'users' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__usersValue = NonSharedList.of(__tmpModified.__usersValue, users);
                return this;
            }

            @Override
            public SysRoleDraft addIntoUsers(DraftConsumer<SysUserDraft> block) {
                addIntoUsers(null, block);
                return this;
            }

            @Override
            public SysRoleDraft addIntoUsers(SysUser base, DraftConsumer<SysUserDraft> block) {
                users(true).add((SysUserDraft)SysUserDraft.$.produce(base, block));
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
                    case SLOT_ROLE_NAME:
                    		setRoleName((String)value);break;
                    case SLOT_ROLE_KEY:
                    		setRoleKey((String)value);break;
                    case SLOT_ROLE_SORT:
                    		if (value == null) throw new IllegalArgumentException("'roleSort' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setRoleSort((Integer)value);
                            break;
                    case SLOT_DATA_SCOPE:
                    		setDataScope((String)value);break;
                    case SLOT_STATUS:
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case SLOT_REMARK:
                    		setRemark((String)value);break;
                    case SLOT_MENUS:
                    		setMenus((List<SysMenu>)value);break;
                    case SLOT_DEPTS:
                    		setDepts((List<SysDept>)value);break;
                    case SLOT_USERS:
                    		setUsers((List<SysUser>)value);break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + prop + "\"");
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
                    case "roleName":
                    		setRoleName((String)value);break;
                    case "roleKey":
                    		setRoleKey((String)value);break;
                    case "roleSort":
                    		if (value == null) throw new IllegalArgumentException("'roleSort' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setRoleSort((Integer)value);
                            break;
                    case "dataScope":
                    		setDataScope((String)value);break;
                    case "status":
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case "remark":
                    		setRemark((String)value);break;
                    case "menus":
                    		setMenus((List<SysMenu>)value);break;
                    case "depts":
                    		setDepts((List<SysDept>)value);break;
                    case "users":
                    		setUsers((List<SysUser>)value);break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + prop + "\"");
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
                    __modified().__visibility = __visibility = Visibility.of(14);
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
                    case SLOT_ROLE_NAME:
                    		__visibility.show(SLOT_ROLE_NAME, visible);break;
                    case SLOT_ROLE_KEY:
                    		__visibility.show(SLOT_ROLE_KEY, visible);break;
                    case SLOT_ROLE_SORT:
                    		__visibility.show(SLOT_ROLE_SORT, visible);break;
                    case SLOT_DATA_SCOPE:
                    		__visibility.show(SLOT_DATA_SCOPE, visible);break;
                    case SLOT_STATUS:
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case SLOT_REMARK:
                    		__visibility.show(SLOT_REMARK, visible);break;
                    case SLOT_MENUS:
                    		__visibility.show(SLOT_MENUS, visible);break;
                    case SLOT_DEPTS:
                    		__visibility.show(SLOT_DEPTS, visible);break;
                    case SLOT_USERS:
                    		__visibility.show(SLOT_USERS, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + 
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
                    __modified().__visibility = __visibility = Visibility.of(14);
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
                    case "roleName":
                    		__visibility.show(SLOT_ROLE_NAME, visible);break;
                    case "roleKey":
                    		__visibility.show(SLOT_ROLE_KEY, visible);break;
                    case "roleSort":
                    		__visibility.show(SLOT_ROLE_SORT, visible);break;
                    case "dataScope":
                    		__visibility.show(SLOT_DATA_SCOPE, visible);break;
                    case "status":
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case "remark":
                    		__visibility.show(SLOT_REMARK, visible);break;
                    case "menus":
                    		__visibility.show(SLOT_MENUS, visible);break;
                    case "depts":
                    		__visibility.show(SLOT_DEPTS, visible);break;
                    case "users":
                    		__visibility.show(SLOT_USERS, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + 
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
                    case SLOT_ROLE_NAME:
                    		__modified().__roleNameValue = null;break;
                    case SLOT_ROLE_KEY:
                    		__modified().__roleKeyValue = null;break;
                    case SLOT_ROLE_SORT:
                    		__modified().__roleSortValue = 0;
                    __modified().__roleSortLoaded = false;break;
                    case SLOT_DATA_SCOPE:
                    		__modified().__dataScopeValue = null;
                    __modified().__dataScopeLoaded = false;break;
                    case SLOT_STATUS:
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case SLOT_REMARK:
                    		__modified().__remarkValue = null;
                    __modified().__remarkLoaded = false;break;
                    case SLOT_MENUS:
                    		__modified().__menusValue = null;break;
                    case SLOT_DEPTS:
                    		__modified().__deptsValue = null;break;
                    case SLOT_USERS:
                    		__modified().__usersValue = null;break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
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
                    case "roleName":
                    		__modified().__roleNameValue = null;break;
                    case "roleKey":
                    		__modified().__roleKeyValue = null;break;
                    case "roleSort":
                    		__modified().__roleSortValue = 0;
                    __modified().__roleSortLoaded = false;break;
                    case "dataScope":
                    		__modified().__dataScopeValue = null;
                    __modified().__dataScopeLoaded = false;break;
                    case "status":
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case "remark":
                    		__modified().__remarkValue = null;
                    __modified().__remarkLoaded = false;break;
                    case "menus":
                    		__modified().__menusValue = null;break;
                    case "depts":
                    		__modified().__deptsValue = null;break;
                    case "users":
                    		__modified().__usersValue = null;break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysRole\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
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
                        if (base.__isLoaded(PropId.byIndex(SLOT_MENUS))) {
                            List<SysMenu> oldValue = base.menus();
                            List<SysMenu> newValue = __ctx.resolveList(oldValue);
                            if (oldValue != newValue) {
                                setMenus(newValue);
                            }
                        }
                        if (base.__isLoaded(PropId.byIndex(SLOT_DEPTS))) {
                            List<SysDept> oldValue = base.depts();
                            List<SysDept> newValue = __ctx.resolveList(oldValue);
                            if (oldValue != newValue) {
                                setDepts(newValue);
                            }
                        }
                        if (base.__isLoaded(PropId.byIndex(SLOT_USERS))) {
                            List<SysUser> oldValue = base.users();
                            List<SysUser> newValue = __ctx.resolveList(oldValue);
                            if (oldValue != newValue) {
                                setUsers(newValue);
                            }
                        }
                        __tmpModified = __modified;
                    }
                    else {
                        __tmpModified.__menusValue = NonSharedList.of(__tmpModified.__menusValue, __ctx.resolveList(__tmpModified.__menusValue));
                        __tmpModified.__deptsValue = NonSharedList.of(__tmpModified.__deptsValue, __ctx.resolveList(__tmpModified.__deptsValue));
                        __tmpModified.__usersValue = NonSharedList.of(__tmpModified.__usersValue, __ctx.resolveList(__tmpModified.__usersValue));
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
            type = SysRole.class
    )
    class Builder {
        private final Producer.DraftImpl __draft;

        public Builder() {
            this(null);
        }

        public Builder(@Nullable SysRole base) {
            __draft = new Producer.DraftImpl(null, base);
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

        public Builder roleName(@NotNull String roleName) {
            if (roleName != null) {
                __draft.setRoleName(roleName);
            }
            return this;
        }

        public Builder roleKey(@NotNull String roleKey) {
            if (roleKey != null) {
                __draft.setRoleKey(roleKey);
            }
            return this;
        }

        public Builder roleSort(@NotNull Integer roleSort) {
            if (roleSort != null) {
                __draft.setRoleSort(roleSort);
            }
            return this;
        }

        public Builder dataScope(@Nullable String dataScope) {
            __draft.setDataScope(dataScope);
            return this;
        }

        public Builder status(@NotNull Integer status) {
            if (status != null) {
                __draft.setStatus(status);
            }
            return this;
        }

        public Builder remark(@Nullable String remark) {
            __draft.setRemark(remark);
            return this;
        }

        public Builder menus(@NotNull List<SysMenu> menus) {
            if (menus != null) {
                __draft.setMenus(menus);
            }
            return this;
        }

        public Builder depts(@NotNull List<SysDept> depts) {
            if (depts != null) {
                __draft.setDepts(depts);
            }
            return this;
        }

        public Builder users(@NotNull List<SysUser> users) {
            if (users != null) {
                __draft.setUsers(users);
            }
            return this;
        }

        public SysRole build() {
            return (SysRole)__draft.__modified();
        }
    }
}
