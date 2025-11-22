package io.github.faustofanb.admin.module.audit.service.impl;

import org.springframework.stereotype.Service;

import io.github.faustofanb.admin.module.audit.domain.entity.SysLogininfor;
import io.github.faustofanb.admin.module.audit.repository.SysLogininforRepository;
import io.github.faustofanb.admin.module.audit.service.SysLogininforService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysLogininforServiceImpl implements SysLogininforService {

    private final SysLogininforRepository sysLogininforRepository;

    @Override
    public void saveLogininfor(SysLogininfor sysLogininfor) {
        sysLogininforRepository.save(sysLogininfor);
    }

    @Override
    public void cleanLogininfor() {
        sysLogininforRepository.deleteAll();
    }
}
