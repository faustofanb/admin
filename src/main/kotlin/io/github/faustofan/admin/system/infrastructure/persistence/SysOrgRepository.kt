package io.github.faustofan.admin.system.infrastructure.persistence

import io.github.faustofan.admin.system.domain.model.SysOrg
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

@Repository
interface SysOrgRepository : KRepository<SysOrg, Long> {

    // 查询根节点 (父节点为空)
//    fun findByParentIsNull(): List<SysOrg>

    // 根据名称模糊搜索
//    fun findByNameLike(name: String): List<SysOrg>

    // 注意：获取子部门通常不需要在这里写方法，
    // 而是通过 Entity 里的 @OneToMany children 属性配合 Fetcher 直接抓取
}