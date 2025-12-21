package io.github.faustofan.admin.system.domain.model;

import io.github.faustofan.admin.shared.persistence.model.TenantAware;
import io.github.faustofan.admin.system.domain.enums.PolicyType;
import org.babyfish.jimmer.sql.*;
import java.util.List;

/**
 * ABAC 策略实体
 * 用于定义基于属性的访问控制策略
 */
@Entity
@Table(name = "sys_abac_policy")
public interface SysAbacPolicy extends TenantAware {

    String name();

    // 策略作用于哪个业务对象/表，例如 "ORDER", "CONTRACT"
    String targetResource();

    // 策略类型: JSON, SPEL (Spring Expression Language), SQL
    PolicyType policyType();

    // 具体的规则内容
    // 例如 SPEL: "#user.attributes['level'] > 5 && #resource.amount < 1000"
    String policyContent();

    @ManyToMany(mappedBy = "policies")
    List<SysRole> roles();
}