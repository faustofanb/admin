package io.github.faustofanb.admin.module.audit.repository;

import java.time.LocalDateTime;

import org.babyfish.jimmer.spring.repository.JRepository;

import io.github.faustofanb.admin.module.audit.domain.entity.SysOperLog;

public interface SysOperLogRepository extends JRepository<SysOperLog, Long> {
    int deleteByOperTimeBefore(LocalDateTime time);
}
