package io.github.faustofanb.admin.module.system.repository;

import io.github.faustofanb.admin.module.system.domain.entity.SysUser;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JRepository<SysUser, Long> {

    Optional<SysUser> findByUserName(String userName);
}
