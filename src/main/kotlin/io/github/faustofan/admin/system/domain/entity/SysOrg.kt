package io.github.faustofan.admin.system.domain.entity

import io.github.faustofan.admin.common.model.TenantAware
import org.babyfish.jimmer.sql.*

@Entity
interface SysOrg : TenantAware {

    val name: String

    // 树形结构：父节点
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE) // 父节点删除时，递归删除子节点
    val parent: SysOrg?

    // 树形结构：子节点
    @OneToMany(mappedBy = "parent")
    val children: List<SysOrg>

    // 关联：该部门下的用户
    @OneToMany(mappedBy = "org")
    val users: List<SysUser>
}