package io.github.faustofanb.admin.module.system.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.faustofanb.admin.core.domain.BaseEntityDraft;
import java.io.Serializable;
import java.lang.Boolean;
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
import org.babyfish.jimmer.sql.ManyToOne;
import org.babyfish.jimmer.sql.OneToMany;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@GeneratedBy(
        type = SysMenu.class
)
public interface SysMenuDraft extends SysMenu, BaseEntityDraft {
    SysMenuDraft.Producer $ = Producer.INSTANCE;

    @OldChain
    SysMenuDraft setId(long id);

    @OldChain
    SysMenuDraft setCreatedTime(LocalDateTime createdTime);

    @OldChain
    SysMenuDraft setUpdatedTime(LocalDateTime updatedTime);

    @OldChain
    SysMenuDraft setCreatedBy(Long createdBy);

    @OldChain
    SysMenuDraft setUpdatedBy(Long updatedBy);

    @OldChain
    SysMenuDraft setParentId(Long parentId);

    @OldChain
    SysMenuDraft setSort(Integer sort);

    @Nullable
    SysMenuDraft parent();

    SysMenuDraft parent(boolean autoCreate);

    @OldChain
    SysMenuDraft setParent(SysMenu parent);

    @OldChain
    SysMenuDraft applyParent(DraftConsumer<SysMenuDraft> block);

    @OldChain
    SysMenuDraft applyParent(SysMenu base, DraftConsumer<SysMenuDraft> block);

    List<SysMenuDraft> children(boolean autoCreate);

    @OldChain
    SysMenuDraft setChildren(List<SysMenu> children);

    @OldChain
    SysMenuDraft addIntoChildren(DraftConsumer<SysMenuDraft> block);

    @OldChain
    SysMenuDraft addIntoChildren(SysMenu base, DraftConsumer<SysMenuDraft> block);

    @OldChain
    SysMenuDraft setMenuName(String menuName);

    @OldChain
    SysMenuDraft setMenuType(String menuType);

    @OldChain
    SysMenuDraft setPath(String path);

    @OldChain
    SysMenuDraft setComponent(String component);

    @OldChain
    SysMenuDraft setPerms(String perms);

    @OldChain
    SysMenuDraft setIcon(String icon);

    @OldChain
    SysMenuDraft setVisible(boolean visible);

    @OldChain
    SysMenuDraft setStatus(int status);

    List<SysRoleDraft> roles(boolean autoCreate);

    @OldChain
    SysMenuDraft setRoles(List<SysRole> roles);

    @OldChain
    SysMenuDraft addIntoRoles(DraftConsumer<SysRoleDraft> block);

    @OldChain
    SysMenuDraft addIntoRoles(SysRole base, DraftConsumer<SysRoleDraft> block);

    @GeneratedBy(
            type = SysMenu.class
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

        public static final int SLOT_MENU_NAME = 9;

        public static final int SLOT_MENU_TYPE = 10;

        public static final int SLOT_PATH = 11;

        public static final int SLOT_COMPONENT = 12;

        public static final int SLOT_PERMS = 13;

        public static final int SLOT_ICON = 14;

        public static final int SLOT_VISIBLE = 15;

        public static final int SLOT_STATUS = 16;

        public static final int SLOT_ROLES = 17;

        public static final ImmutableType TYPE = ImmutableType
            .newBuilder(
                "0.9.96",
                SysMenu.class,
                Collections.singleton(BaseEntityDraft.Producer.TYPE),
                (ctx, base) -> new DraftImpl(ctx, (SysMenu)base)
            )
            .redefine("id", SLOT_ID)
            .redefine("createdTime", SLOT_CREATED_TIME)
            .redefine("updatedTime", SLOT_UPDATED_TIME)
            .redefine("createdBy", SLOT_CREATED_BY)
            .redefine("updatedBy", SLOT_UPDATED_BY)
            .add(SLOT_PARENT_ID, "parentId", ImmutablePropCategory.SCALAR, Long.class, true)
            .add(SLOT_SORT, "sort", ImmutablePropCategory.SCALAR, Integer.class, true)
            .add(SLOT_PARENT, "parent", ManyToOne.class, SysMenu.class, true)
            .add(SLOT_CHILDREN, "children", OneToMany.class, SysMenu.class, false)
            .add(SLOT_MENU_NAME, "menuName", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_MENU_TYPE, "menuType", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_PATH, "path", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_COMPONENT, "component", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_PERMS, "perms", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_ICON, "icon", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_VISIBLE, "visible", ImmutablePropCategory.SCALAR, boolean.class, false)
            .add(SLOT_STATUS, "status", ImmutablePropCategory.SCALAR, int.class, false)
            .add(SLOT_ROLES, "roles", ManyToMany.class, SysRole.class, false)
            .build();

        private Producer() {
        }

        public SysMenu produce(DraftConsumer<SysMenuDraft> block) {
            return (SysMenu)Internal.produce(TYPE, null, block);
        }

        public SysMenu produce(SysMenu base, DraftConsumer<SysMenuDraft> block) {
            return (SysMenu)Internal.produce(TYPE, base, block);
        }

        public SysMenu produce(boolean resolveImmediately, DraftConsumer<SysMenuDraft> block) {
            return (SysMenu)Internal.produce(TYPE, null, resolveImmediately, block);
        }

        public SysMenu produce(SysMenu base, boolean resolveImmediately,
                DraftConsumer<SysMenuDraft> block) {
            return (SysMenu)Internal.produce(TYPE, base, resolveImmediately, block);
        }

        /**
         * Class, not interface, for free-marker
         */
        @GeneratedBy(
                type = SysMenu.class
        )
        @JsonPropertyOrder({"dummyPropForJacksonError__", "id", "createdTime", "updatedTime", "createdBy", "updatedBy", "parentId", "sort", "parent", "children", "menuName", "menuType", "path", "component", "perms", "icon", "visible", "status", "roles"})
        public abstract static class Implementor implements SysMenu, ImmutableSpi {
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
                    case SLOT_MENU_NAME:
                    		return menuName();
                    case SLOT_MENU_TYPE:
                    		return menuType();
                    case SLOT_PATH:
                    		return path();
                    case SLOT_COMPONENT:
                    		return component();
                    case SLOT_PERMS:
                    		return perms();
                    case SLOT_ICON:
                    		return icon();
                    case SLOT_VISIBLE:
                    		return (Boolean)visible();
                    case SLOT_STATUS:
                    		return (Integer)status();
                    case SLOT_ROLES:
                    		return roles();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + prop + "\"");
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
                    case "menuName":
                    		return menuName();
                    case "menuType":
                    		return menuType();
                    case "path":
                    		return path();
                    case "component":
                    		return component();
                    case "perms":
                    		return perms();
                    case "icon":
                    		return icon();
                    case "visible":
                    		return (Boolean)visible();
                    case "status":
                    		return (Integer)status();
                    case "roles":
                    		return roles();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + prop + "\"");
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
            public final SysMenu getParent() {
                return parent();
            }

            public final List<SysMenu> getChildren() {
                return children();
            }

            public final String getMenuName() {
                return menuName();
            }

            public final String getMenuType() {
                return menuType();
            }

            @Nullable
            public final String getPath() {
                return path();
            }

            @Nullable
            public final String getComponent() {
                return component();
            }

            @Nullable
            public final String getPerms() {
                return perms();
            }

            @Nullable
            public final String getIcon() {
                return icon();
            }

            public final boolean isVisible() {
                return visible();
            }

            public final int getStatus() {
                return status();
            }

            public final List<SysRole> getRoles() {
                return roles();
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
                type = SysMenu.class
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

            SysMenu __parentValue;

            boolean __parentLoaded = false;

            NonSharedList<SysMenu> __childrenValue;

            String __menuNameValue;

            String __menuTypeValue;

            String __pathValue;

            boolean __pathLoaded = false;

            String __componentValue;

            boolean __componentLoaded = false;

            String __permsValue;

            boolean __permsLoaded = false;

            String __iconValue;

            boolean __iconLoaded = false;

            boolean __visibleValue;

            boolean __visibleLoaded = false;

            int __statusValue;

            boolean __statusLoaded = false;

            NonSharedList<SysRole> __rolesValue;

            Impl() {
                __visibility = Visibility.of(18);
                __visibility.show(SLOT_PARENT_ID, false);
            }

            @Override
            @JsonIgnore
            public long id() {
                if (!__idLoaded) {
                    throw new UnloadedException(SysMenu.class, "id");
                }
                return __idValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime createdTime() {
                if (__createdTimeValue == null) {
                    throw new UnloadedException(SysMenu.class, "createdTime");
                }
                return __createdTimeValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime updatedTime() {
                if (__updatedTimeValue == null) {
                    throw new UnloadedException(SysMenu.class, "updatedTime");
                }
                return __updatedTimeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long createdBy() {
                if (!__createdByLoaded) {
                    throw new UnloadedException(SysMenu.class, "createdBy");
                }
                return __createdByValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long updatedBy() {
                if (!__updatedByLoaded) {
                    throw new UnloadedException(SysMenu.class, "updatedBy");
                }
                return __updatedByValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long parentId() {
                SysMenu __target = parent();
                return __target != null ? __target.id() : null;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Integer sort() {
                if (!__sortLoaded) {
                    throw new UnloadedException(SysMenu.class, "sort");
                }
                return __sortValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public SysMenu parent() {
                if (!__parentLoaded) {
                    throw new UnloadedException(SysMenu.class, "parent");
                }
                return __parentValue;
            }

            @Override
            @JsonIgnore
            public List<SysMenu> children() {
                if (__childrenValue == null) {
                    throw new UnloadedException(SysMenu.class, "children");
                }
                return __childrenValue;
            }

            @Override
            @JsonIgnore
            @Description("菜单名称")
            public String menuName() {
                if (__menuNameValue == null) {
                    throw new UnloadedException(SysMenu.class, "menuName");
                }
                return __menuNameValue;
            }

            @Override
            @JsonIgnore
            @Description("菜单类型（M目录 C菜单 F按钮）")
            public String menuType() {
                if (__menuTypeValue == null) {
                    throw new UnloadedException(SysMenu.class, "menuType");
                }
                return __menuTypeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("路由地址")
            public String path() {
                if (!__pathLoaded) {
                    throw new UnloadedException(SysMenu.class, "path");
                }
                return __pathValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("组件路径")
            public String component() {
                if (!__componentLoaded) {
                    throw new UnloadedException(SysMenu.class, "component");
                }
                return __componentValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("权限标识")
            public String perms() {
                if (!__permsLoaded) {
                    throw new UnloadedException(SysMenu.class, "perms");
                }
                return __permsValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("菜单图标")
            public String icon() {
                if (!__iconLoaded) {
                    throw new UnloadedException(SysMenu.class, "icon");
                }
                return __iconValue;
            }

            @Override
            @JsonIgnore
            @Description("是否显示")
            public boolean visible() {
                if (!__visibleLoaded) {
                    throw new UnloadedException(SysMenu.class, "visible");
                }
                return __visibleValue;
            }

            @Override
            @JsonIgnore
            @Description("菜单状态（0正常 1停用）")
            public int status() {
                if (!__statusLoaded) {
                    throw new UnloadedException(SysMenu.class, "status");
                }
                return __statusValue;
            }

            @Override
            @JsonIgnore
            public List<SysRole> roles() {
                if (__rolesValue == null) {
                    throw new UnloadedException(SysMenu.class, "roles");
                }
                return __rolesValue;
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
                    case SLOT_MENU_NAME:
                    		return __menuNameValue != null;
                    case SLOT_MENU_TYPE:
                    		return __menuTypeValue != null;
                    case SLOT_PATH:
                    		return __pathLoaded;
                    case SLOT_COMPONENT:
                    		return __componentLoaded;
                    case SLOT_PERMS:
                    		return __permsLoaded;
                    case SLOT_ICON:
                    		return __iconLoaded;
                    case SLOT_VISIBLE:
                    		return __visibleLoaded;
                    case SLOT_STATUS:
                    		return __statusLoaded;
                    case SLOT_ROLES:
                    		return __rolesValue != null;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + prop + "\"");
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
                    case "menuName":
                    		return __menuNameValue != null;
                    case "menuType":
                    		return __menuTypeValue != null;
                    case "path":
                    		return __pathLoaded;
                    case "component":
                    		return __componentLoaded;
                    case "perms":
                    		return __permsLoaded;
                    case "icon":
                    		return __iconLoaded;
                    case "visible":
                    		return __visibleLoaded;
                    case "status":
                    		return __statusLoaded;
                    case "roles":
                    		return __rolesValue != null;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + prop + "\"");
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
                    case SLOT_MENU_NAME:
                    		return __visibility.visible(SLOT_MENU_NAME);
                    case SLOT_MENU_TYPE:
                    		return __visibility.visible(SLOT_MENU_TYPE);
                    case SLOT_PATH:
                    		return __visibility.visible(SLOT_PATH);
                    case SLOT_COMPONENT:
                    		return __visibility.visible(SLOT_COMPONENT);
                    case SLOT_PERMS:
                    		return __visibility.visible(SLOT_PERMS);
                    case SLOT_ICON:
                    		return __visibility.visible(SLOT_ICON);
                    case SLOT_VISIBLE:
                    		return __visibility.visible(SLOT_VISIBLE);
                    case SLOT_STATUS:
                    		return __visibility.visible(SLOT_STATUS);
                    case SLOT_ROLES:
                    		return __visibility.visible(SLOT_ROLES);
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
                    case "menuName":
                    		return __visibility.visible(SLOT_MENU_NAME);
                    case "menuType":
                    		return __visibility.visible(SLOT_MENU_TYPE);
                    case "path":
                    		return __visibility.visible(SLOT_PATH);
                    case "component":
                    		return __visibility.visible(SLOT_COMPONENT);
                    case "perms":
                    		return __visibility.visible(SLOT_PERMS);
                    case "icon":
                    		return __visibility.visible(SLOT_ICON);
                    case "visible":
                    		return __visibility.visible(SLOT_VISIBLE);
                    case "status":
                    		return __visibility.visible(SLOT_STATUS);
                    case "roles":
                    		return __visibility.visible(SLOT_ROLES);
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
                if (__menuNameValue != null) {
                    hash = 31 * hash + __menuNameValue.hashCode();
                }
                if (__menuTypeValue != null) {
                    hash = 31 * hash + __menuTypeValue.hashCode();
                }
                if (__pathLoaded && __pathValue != null) {
                    hash = 31 * hash + __pathValue.hashCode();
                }
                if (__componentLoaded && __componentValue != null) {
                    hash = 31 * hash + __componentValue.hashCode();
                }
                if (__permsLoaded && __permsValue != null) {
                    hash = 31 * hash + __permsValue.hashCode();
                }
                if (__iconLoaded && __iconValue != null) {
                    hash = 31 * hash + __iconValue.hashCode();
                }
                if (__visibleLoaded) {
                    hash = 31 * hash + Boolean.hashCode(__visibleValue);
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__rolesValue != null) {
                    hash = 31 * hash + __rolesValue.hashCode();
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
                if (__menuNameValue != null) {
                    hash = 31 * hash + System.identityHashCode(__menuNameValue);
                }
                if (__menuTypeValue != null) {
                    hash = 31 * hash + System.identityHashCode(__menuTypeValue);
                }
                if (__pathLoaded) {
                    hash = 31 * hash + System.identityHashCode(__pathValue);
                }
                if (__componentLoaded) {
                    hash = 31 * hash + System.identityHashCode(__componentValue);
                }
                if (__permsLoaded) {
                    hash = 31 * hash + System.identityHashCode(__permsValue);
                }
                if (__iconLoaded) {
                    hash = 31 * hash + System.identityHashCode(__iconValue);
                }
                if (__visibleLoaded) {
                    hash = 31 * hash + Boolean.hashCode(__visibleValue);
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__rolesValue != null) {
                    hash = 31 * hash + System.identityHashCode(__rolesValue);
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
                if (__isVisible(PropId.byIndex(SLOT_MENU_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_MENU_NAME))) {
                    return false;
                }
                boolean __menuNameLoaded = __menuNameValue != null;
                if (__menuNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_MENU_NAME))) {
                    return false;
                }
                if (__menuNameLoaded && !Objects.equals(__menuNameValue, __other.menuName())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_MENU_TYPE)) != __other.__isVisible(PropId.byIndex(SLOT_MENU_TYPE))) {
                    return false;
                }
                boolean __menuTypeLoaded = __menuTypeValue != null;
                if (__menuTypeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_MENU_TYPE))) {
                    return false;
                }
                if (__menuTypeLoaded && !Objects.equals(__menuTypeValue, __other.menuType())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PATH)) != __other.__isVisible(PropId.byIndex(SLOT_PATH))) {
                    return false;
                }
                boolean __pathLoaded = this.__pathLoaded;
                if (__pathLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PATH))) {
                    return false;
                }
                if (__pathLoaded && !Objects.equals(__pathValue, __other.path())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_COMPONENT)) != __other.__isVisible(PropId.byIndex(SLOT_COMPONENT))) {
                    return false;
                }
                boolean __componentLoaded = this.__componentLoaded;
                if (__componentLoaded != __other.__isLoaded(PropId.byIndex(SLOT_COMPONENT))) {
                    return false;
                }
                if (__componentLoaded && !Objects.equals(__componentValue, __other.component())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PERMS)) != __other.__isVisible(PropId.byIndex(SLOT_PERMS))) {
                    return false;
                }
                boolean __permsLoaded = this.__permsLoaded;
                if (__permsLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PERMS))) {
                    return false;
                }
                if (__permsLoaded && !Objects.equals(__permsValue, __other.perms())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ICON)) != __other.__isVisible(PropId.byIndex(SLOT_ICON))) {
                    return false;
                }
                boolean __iconLoaded = this.__iconLoaded;
                if (__iconLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ICON))) {
                    return false;
                }
                if (__iconLoaded && !Objects.equals(__iconValue, __other.icon())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_VISIBLE)) != __other.__isVisible(PropId.byIndex(SLOT_VISIBLE))) {
                    return false;
                }
                boolean __visibleLoaded = this.__visibleLoaded;
                if (__visibleLoaded != __other.__isLoaded(PropId.byIndex(SLOT_VISIBLE))) {
                    return false;
                }
                if (__visibleLoaded && __visibleValue != __other.visible()) {
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
                if (__isVisible(PropId.byIndex(SLOT_ROLES)) != __other.__isVisible(PropId.byIndex(SLOT_ROLES))) {
                    return false;
                }
                boolean __rolesLoaded = __rolesValue != null;
                if (__rolesLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLES))) {
                    return false;
                }
                if (__rolesLoaded && !Objects.equals(__rolesValue, __other.roles())) {
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
                if (__isVisible(PropId.byIndex(SLOT_MENU_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_MENU_NAME))) {
                    return false;
                }
                boolean __menuNameLoaded = __menuNameValue != null;
                if (__menuNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_MENU_NAME))) {
                    return false;
                }
                if (__menuNameLoaded && __menuNameValue != __other.menuName()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_MENU_TYPE)) != __other.__isVisible(PropId.byIndex(SLOT_MENU_TYPE))) {
                    return false;
                }
                boolean __menuTypeLoaded = __menuTypeValue != null;
                if (__menuTypeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_MENU_TYPE))) {
                    return false;
                }
                if (__menuTypeLoaded && __menuTypeValue != __other.menuType()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PATH)) != __other.__isVisible(PropId.byIndex(SLOT_PATH))) {
                    return false;
                }
                boolean __pathLoaded = this.__pathLoaded;
                if (__pathLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PATH))) {
                    return false;
                }
                if (__pathLoaded && __pathValue != __other.path()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_COMPONENT)) != __other.__isVisible(PropId.byIndex(SLOT_COMPONENT))) {
                    return false;
                }
                boolean __componentLoaded = this.__componentLoaded;
                if (__componentLoaded != __other.__isLoaded(PropId.byIndex(SLOT_COMPONENT))) {
                    return false;
                }
                if (__componentLoaded && __componentValue != __other.component()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PERMS)) != __other.__isVisible(PropId.byIndex(SLOT_PERMS))) {
                    return false;
                }
                boolean __permsLoaded = this.__permsLoaded;
                if (__permsLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PERMS))) {
                    return false;
                }
                if (__permsLoaded && __permsValue != __other.perms()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ICON)) != __other.__isVisible(PropId.byIndex(SLOT_ICON))) {
                    return false;
                }
                boolean __iconLoaded = this.__iconLoaded;
                if (__iconLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ICON))) {
                    return false;
                }
                if (__iconLoaded && __iconValue != __other.icon()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_VISIBLE)) != __other.__isVisible(PropId.byIndex(SLOT_VISIBLE))) {
                    return false;
                }
                boolean __visibleLoaded = this.__visibleLoaded;
                if (__visibleLoaded != __other.__isLoaded(PropId.byIndex(SLOT_VISIBLE))) {
                    return false;
                }
                if (__visibleLoaded && __visibleValue != __other.visible()) {
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
                if (__isVisible(PropId.byIndex(SLOT_ROLES)) != __other.__isVisible(PropId.byIndex(SLOT_ROLES))) {
                    return false;
                }
                boolean __rolesLoaded = __rolesValue != null;
                if (__rolesLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLES))) {
                    return false;
                }
                if (__rolesLoaded && __rolesValue != __other.roles()) {
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
                type = SysMenu.class
        )
        private static class DraftImpl extends Implementor implements DraftSpi, SysMenuDraft {
            private DraftContext __ctx;

            private Impl __base;

            private Impl __modified;

            private boolean __resolving;

            private SysMenu __resolved;

            DraftImpl(DraftContext ctx, SysMenu base) {
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
            public SysMenuDraft setId(long id) {
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
            public SysMenuDraft setCreatedTime(LocalDateTime createdTime) {
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
            public SysMenuDraft setUpdatedTime(LocalDateTime updatedTime) {
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
            public SysMenuDraft setCreatedBy(Long createdBy) {
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
            public SysMenuDraft setUpdatedBy(Long updatedBy) {
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
                SysMenu __target = parent();
                return __target != null ? __target.id() : null;
            }

            @Override
            public SysMenuDraft setParentId(Long parentId) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (parentId != null) {
                    setParent(ImmutableObjects.makeIdOnly(SysMenu.class, parentId));
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
            public SysMenuDraft setSort(Integer sort) {
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
            public SysMenuDraft parent() {
                return __ctx.toDraftObject((__modified!= null ? __modified : __base).parent());
            }

            @Override
            public SysMenuDraft parent(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_PARENT)) || parent() == null)) {
                    setParent(SysMenuDraft.$.produce(null, null));
                }
                return __ctx.toDraftObject((__modified!= null ? __modified : __base).parent());
            }

            @Override
            public SysMenuDraft setParent(SysMenu parent) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__parentValue = parent;
                __tmpModified.__parentLoaded = true;
                return this;
            }

            @Override
            public SysMenuDraft applyParent(DraftConsumer<SysMenuDraft> block) {
                applyParent(null, block);
                return this;
            }

            @Override
            public SysMenuDraft applyParent(SysMenu base, DraftConsumer<SysMenuDraft> block) {
                setParent(SysMenuDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            public List<SysMenu> children() {
                return __ctx.toDraftList((__modified!= null ? __modified : __base).children(), SysMenu.class, true);
            }

            @Override
            public List<SysMenuDraft> children(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_CHILDREN)))) {
                    setChildren(new ArrayList<>());
                }
                return __ctx.toDraftList((__modified!= null ? __modified : __base).children(), SysMenu.class, true);
            }

            @Override
            public SysMenuDraft setChildren(List<SysMenu> children) {
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
            public SysMenuDraft addIntoChildren(DraftConsumer<SysMenuDraft> block) {
                addIntoChildren(null, block);
                return this;
            }

            @Override
            public SysMenuDraft addIntoChildren(SysMenu base, DraftConsumer<SysMenuDraft> block) {
                children(true).add((SysMenuDraft)SysMenuDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            public String menuName() {
                return (__modified!= null ? __modified : __base).menuName();
            }

            @Override
            public SysMenuDraft setMenuName(String menuName) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (menuName == null) {
                    throw new IllegalArgumentException(
                        "'menuName' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__menuNameValue = menuName;
                return this;
            }

            @Override
            @JsonIgnore
            public String menuType() {
                return (__modified!= null ? __modified : __base).menuType();
            }

            @Override
            public SysMenuDraft setMenuType(String menuType) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (menuType == null) {
                    throw new IllegalArgumentException(
                        "'menuType' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__menuTypeValue = menuType;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String path() {
                return (__modified!= null ? __modified : __base).path();
            }

            @Override
            public SysMenuDraft setPath(String path) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__pathValue = path;
                __tmpModified.__pathLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String component() {
                return (__modified!= null ? __modified : __base).component();
            }

            @Override
            public SysMenuDraft setComponent(String component) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__componentValue = component;
                __tmpModified.__componentLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String perms() {
                return (__modified!= null ? __modified : __base).perms();
            }

            @Override
            public SysMenuDraft setPerms(String perms) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__permsValue = perms;
                __tmpModified.__permsLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String icon() {
                return (__modified!= null ? __modified : __base).icon();
            }

            @Override
            public SysMenuDraft setIcon(String icon) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__iconValue = icon;
                __tmpModified.__iconLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public boolean visible() {
                return (__modified!= null ? __modified : __base).visible();
            }

            @Override
            public SysMenuDraft setVisible(boolean visible) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__visibleValue = visible;
                __tmpModified.__visibleLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public int status() {
                return (__modified!= null ? __modified : __base).status();
            }

            @Override
            public SysMenuDraft setStatus(int status) {
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
            public List<SysRole> roles() {
                return __ctx.toDraftList((__modified!= null ? __modified : __base).roles(), SysRole.class, true);
            }

            @Override
            public List<SysRoleDraft> roles(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_ROLES)))) {
                    setRoles(new ArrayList<>());
                }
                return __ctx.toDraftList((__modified!= null ? __modified : __base).roles(), SysRole.class, true);
            }

            @Override
            public SysMenuDraft setRoles(List<SysRole> roles) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (roles == null) {
                    throw new IllegalArgumentException(
                        "'roles' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__rolesValue = NonSharedList.of(__tmpModified.__rolesValue, roles);
                return this;
            }

            @Override
            public SysMenuDraft addIntoRoles(DraftConsumer<SysRoleDraft> block) {
                addIntoRoles(null, block);
                return this;
            }

            @Override
            public SysMenuDraft addIntoRoles(SysRole base, DraftConsumer<SysRoleDraft> block) {
                roles(true).add((SysRoleDraft)SysRoleDraft.$.produce(base, block));
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
                    		setParent((SysMenu)value);break;
                    case SLOT_CHILDREN:
                    		setChildren((List<SysMenu>)value);break;
                    case SLOT_MENU_NAME:
                    		setMenuName((String)value);break;
                    case SLOT_MENU_TYPE:
                    		setMenuType((String)value);break;
                    case SLOT_PATH:
                    		setPath((String)value);break;
                    case SLOT_COMPONENT:
                    		setComponent((String)value);break;
                    case SLOT_PERMS:
                    		setPerms((String)value);break;
                    case SLOT_ICON:
                    		setIcon((String)value);break;
                    case SLOT_VISIBLE:
                    		if (value == null) throw new IllegalArgumentException("'visible' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setVisible((Boolean)value);
                            break;
                    case SLOT_STATUS:
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case SLOT_ROLES:
                    		setRoles((List<SysRole>)value);break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + prop + "\"");
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
                    		setParent((SysMenu)value);break;
                    case "children":
                    		setChildren((List<SysMenu>)value);break;
                    case "menuName":
                    		setMenuName((String)value);break;
                    case "menuType":
                    		setMenuType((String)value);break;
                    case "path":
                    		setPath((String)value);break;
                    case "component":
                    		setComponent((String)value);break;
                    case "perms":
                    		setPerms((String)value);break;
                    case "icon":
                    		setIcon((String)value);break;
                    case "visible":
                    		if (value == null) throw new IllegalArgumentException("'visible' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setVisible((Boolean)value);
                            break;
                    case "status":
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case "roles":
                    		setRoles((List<SysRole>)value);break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + prop + "\"");
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
                    __modified().__visibility = __visibility = Visibility.of(18);
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
                    case SLOT_MENU_NAME:
                    		__visibility.show(SLOT_MENU_NAME, visible);break;
                    case SLOT_MENU_TYPE:
                    		__visibility.show(SLOT_MENU_TYPE, visible);break;
                    case SLOT_PATH:
                    		__visibility.show(SLOT_PATH, visible);break;
                    case SLOT_COMPONENT:
                    		__visibility.show(SLOT_COMPONENT, visible);break;
                    case SLOT_PERMS:
                    		__visibility.show(SLOT_PERMS, visible);break;
                    case SLOT_ICON:
                    		__visibility.show(SLOT_ICON, visible);break;
                    case SLOT_VISIBLE:
                    		__visibility.show(SLOT_VISIBLE, visible);break;
                    case SLOT_STATUS:
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case SLOT_ROLES:
                    		__visibility.show(SLOT_ROLES, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + 
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
                    __modified().__visibility = __visibility = Visibility.of(18);
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
                    case "menuName":
                    		__visibility.show(SLOT_MENU_NAME, visible);break;
                    case "menuType":
                    		__visibility.show(SLOT_MENU_TYPE, visible);break;
                    case "path":
                    		__visibility.show(SLOT_PATH, visible);break;
                    case "component":
                    		__visibility.show(SLOT_COMPONENT, visible);break;
                    case "perms":
                    		__visibility.show(SLOT_PERMS, visible);break;
                    case "icon":
                    		__visibility.show(SLOT_ICON, visible);break;
                    case "visible":
                    		__visibility.show(SLOT_VISIBLE, visible);break;
                    case "status":
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case "roles":
                    		__visibility.show(SLOT_ROLES, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + 
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
                    case SLOT_MENU_NAME:
                    		__modified().__menuNameValue = null;break;
                    case SLOT_MENU_TYPE:
                    		__modified().__menuTypeValue = null;break;
                    case SLOT_PATH:
                    		__modified().__pathValue = null;
                    __modified().__pathLoaded = false;break;
                    case SLOT_COMPONENT:
                    		__modified().__componentValue = null;
                    __modified().__componentLoaded = false;break;
                    case SLOT_PERMS:
                    		__modified().__permsValue = null;
                    __modified().__permsLoaded = false;break;
                    case SLOT_ICON:
                    		__modified().__iconValue = null;
                    __modified().__iconLoaded = false;break;
                    case SLOT_VISIBLE:
                    		__modified().__visibleValue = false;
                    __modified().__visibleLoaded = false;break;
                    case SLOT_STATUS:
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case SLOT_ROLES:
                    		__modified().__rolesValue = null;break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
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
                    case "menuName":
                    		__modified().__menuNameValue = null;break;
                    case "menuType":
                    		__modified().__menuTypeValue = null;break;
                    case "path":
                    		__modified().__pathValue = null;
                    __modified().__pathLoaded = false;break;
                    case "component":
                    		__modified().__componentValue = null;
                    __modified().__componentLoaded = false;break;
                    case "perms":
                    		__modified().__permsValue = null;
                    __modified().__permsLoaded = false;break;
                    case "icon":
                    		__modified().__iconValue = null;
                    __modified().__iconLoaded = false;break;
                    case "visible":
                    		__modified().__visibleValue = false;
                    __modified().__visibleLoaded = false;break;
                    case "status":
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case "roles":
                    		__modified().__rolesValue = null;break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysMenu\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
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
                            SysMenu oldValue = base.parent();
                            SysMenu newValue = __ctx.resolveObject(oldValue);
                            if (oldValue != newValue) {
                                setParent(newValue);
                            }
                        }
                        if (base.__isLoaded(PropId.byIndex(SLOT_CHILDREN))) {
                            List<SysMenu> oldValue = base.children();
                            List<SysMenu> newValue = __ctx.resolveList(oldValue);
                            if (oldValue != newValue) {
                                setChildren(newValue);
                            }
                        }
                        if (base.__isLoaded(PropId.byIndex(SLOT_ROLES))) {
                            List<SysRole> oldValue = base.roles();
                            List<SysRole> newValue = __ctx.resolveList(oldValue);
                            if (oldValue != newValue) {
                                setRoles(newValue);
                            }
                        }
                        __tmpModified = __modified;
                    }
                    else {
                        __tmpModified.__parentValue = __ctx.resolveObject(__tmpModified.__parentValue);
                        __tmpModified.__childrenValue = NonSharedList.of(__tmpModified.__childrenValue, __ctx.resolveList(__tmpModified.__childrenValue));
                        __tmpModified.__rolesValue = NonSharedList.of(__tmpModified.__rolesValue, __ctx.resolveList(__tmpModified.__rolesValue));
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
            type = SysMenu.class
    )
    class Builder {
        private final Producer.DraftImpl __draft;

        public Builder() {
            this(null);
        }

        public Builder(@Nullable SysMenu base) {
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

        public Builder parent(@Nullable SysMenu parent) {
            __draft.setParent(parent);
            __draft.__show(PropId.byIndex(Producer.SLOT_PARENT), true);
            return this;
        }

        public Builder children(@NotNull List<SysMenu> children) {
            if (children != null) {
                __draft.setChildren(children);
            }
            return this;
        }

        public Builder menuName(@NotNull String menuName) {
            if (menuName != null) {
                __draft.setMenuName(menuName);
            }
            return this;
        }

        public Builder menuType(@NotNull String menuType) {
            if (menuType != null) {
                __draft.setMenuType(menuType);
            }
            return this;
        }

        public Builder path(@Nullable String path) {
            __draft.setPath(path);
            return this;
        }

        public Builder component(@Nullable String component) {
            __draft.setComponent(component);
            return this;
        }

        public Builder perms(@Nullable String perms) {
            __draft.setPerms(perms);
            return this;
        }

        public Builder icon(@Nullable String icon) {
            __draft.setIcon(icon);
            return this;
        }

        public Builder visible(@NotNull Boolean visible) {
            if (visible != null) {
                __draft.setVisible(visible);
            }
            return this;
        }

        public Builder status(@NotNull Integer status) {
            if (status != null) {
                __draft.setStatus(status);
            }
            return this;
        }

        public Builder roles(@NotNull List<SysRole> roles) {
            if (roles != null) {
                __draft.setRoles(roles);
            }
            return this;
        }

        public SysMenu build() {
            return (SysMenu)__draft.__modified();
        }
    }
}
