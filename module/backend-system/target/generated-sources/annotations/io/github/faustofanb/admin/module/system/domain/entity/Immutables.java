package io.github.faustofanb.admin.module.system.domain.entity;

import org.babyfish.jimmer.DraftConsumer;
import org.babyfish.jimmer.internal.GeneratedBy;

@GeneratedBy
public interface Immutables {
    static SysConfig createSysConfig(DraftConsumer<SysConfigDraft> block) {
        return SysConfigDraft.$.produce(block);
    }

    static SysConfig createSysConfig(SysConfig base, DraftConsumer<SysConfigDraft> block) {
        return SysConfigDraft.$.produce(base, block);
    }

    static SysDept createSysDept(DraftConsumer<SysDeptDraft> block) {
        return SysDeptDraft.$.produce(block);
    }

    static SysDept createSysDept(SysDept base, DraftConsumer<SysDeptDraft> block) {
        return SysDeptDraft.$.produce(base, block);
    }

    static SysDictData createSysDictData(DraftConsumer<SysDictDataDraft> block) {
        return SysDictDataDraft.$.produce(block);
    }

    static SysDictData createSysDictData(SysDictData base, DraftConsumer<SysDictDataDraft> block) {
        return SysDictDataDraft.$.produce(base, block);
    }

    static SysDictType createSysDictType(DraftConsumer<SysDictTypeDraft> block) {
        return SysDictTypeDraft.$.produce(block);
    }

    static SysDictType createSysDictType(SysDictType base, DraftConsumer<SysDictTypeDraft> block) {
        return SysDictTypeDraft.$.produce(base, block);
    }

    static SysMenu createSysMenu(DraftConsumer<SysMenuDraft> block) {
        return SysMenuDraft.$.produce(block);
    }

    static SysMenu createSysMenu(SysMenu base, DraftConsumer<SysMenuDraft> block) {
        return SysMenuDraft.$.produce(base, block);
    }

    static SysPost createSysPost(DraftConsumer<SysPostDraft> block) {
        return SysPostDraft.$.produce(block);
    }

    static SysPost createSysPost(SysPost base, DraftConsumer<SysPostDraft> block) {
        return SysPostDraft.$.produce(base, block);
    }

    static SysRole createSysRole(DraftConsumer<SysRoleDraft> block) {
        return SysRoleDraft.$.produce(block);
    }

    static SysRole createSysRole(SysRole base, DraftConsumer<SysRoleDraft> block) {
        return SysRoleDraft.$.produce(base, block);
    }

    static SysUser createSysUser(DraftConsumer<SysUserDraft> block) {
        return SysUserDraft.$.produce(block);
    }

    static SysUser createSysUser(SysUser base, DraftConsumer<SysUserDraft> block) {
        return SysUserDraft.$.produce(base, block);
    }
}
