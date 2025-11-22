package io.github.faustofanb.admin.rpc.impl.system;

import io.github.faustofanb.admin.rpc.api.model.SysLogininforDTO;
import io.github.faustofanb.admin.rpc.api.model.SysOperLogDTO;
import io.github.faustofanb.admin.rpc.api.system.RemoteLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoteLogServiceImpl implements RemoteLogService {

    @Override
    public boolean saveLog(SysOperLogDTO log) {
        // TODO: Implement when Audit module is ready
        return true;
    }

    @Override
    public boolean saveLogininfor(SysLogininforDTO info) {
        // TODO: Implement when Audit module is ready
        return true;
    }
}
