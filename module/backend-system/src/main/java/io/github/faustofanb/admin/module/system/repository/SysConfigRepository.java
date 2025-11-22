package io.github.faustofanb.admin.module.system.repository;

import io.github.faustofanb.admin.module.system.domain.entity.SysConfig;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysConfigRepository extends JRepository<SysConfig, Long> {

    Optional<SysConfig> findByConfigKey(String configKey);
}
