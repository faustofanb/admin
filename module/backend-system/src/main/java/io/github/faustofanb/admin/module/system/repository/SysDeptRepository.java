package io.github.faustofanb.admin.module.system.repository;

import io.github.faustofanb.admin.module.system.domain.entity.SysDept;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDeptRepository extends JRepository<SysDept, Long> {
}
