package io.github.faustofanb.admin.module.file.repository;

import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import io.github.faustofanb.admin.module.file.domain.entity.SysFile;

@Repository
public interface SysFileRepository extends JRepository<SysFile, Long> {
}
