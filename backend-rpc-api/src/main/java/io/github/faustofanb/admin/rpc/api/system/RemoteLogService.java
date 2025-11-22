package io.github.faustofanb.admin.rpc.api.system;

import io.github.faustofanb.admin.rpc.api.model.SysLogininforDTO;
import io.github.faustofanb.admin.rpc.api.model.SysOperLogDTO;

public interface RemoteLogService {

    boolean saveLog(SysOperLogDTO log);

    boolean saveLogininfor(SysLogininforDTO info);
}
