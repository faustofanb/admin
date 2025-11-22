package io.github.faustofanb.admin.module.audit.service;

import io.github.faustofanb.admin.module.audit.domain.entity.SysLogininfor;

public interface SysLogininforService {
    void saveLogininfor(SysLogininfor sysLogininfor);

    void cleanLogininfor();
}
