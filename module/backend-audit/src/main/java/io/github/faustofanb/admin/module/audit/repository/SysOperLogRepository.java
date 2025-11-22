package io.github.faustofanb.admin.module.audit.repository;

import org.babyfish.jimmer.spring.repository.JRepository;

import io.github.faustofanb.admin.module.audit.domain.entity.SysOperLog;

public interface SysOperLogRepository extends JRepository<SysOperLog, Long> {
}
