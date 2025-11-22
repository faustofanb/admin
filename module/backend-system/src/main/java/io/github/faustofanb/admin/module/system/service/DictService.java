package io.github.faustofanb.admin.module.system.service;

import io.github.faustofanb.admin.module.system.domain.entity.SysDictData;
import io.github.faustofanb.admin.module.system.domain.entity.SysDictType;

import java.util.List;

public interface DictService {

    List<SysDictData> selectDictDataByType(String dictType);

    void deleteDictTypeByIds(List<Long> ids);

    void deleteDictDataByIds(List<Long> ids);
    
    // Add other CRUD methods as needed
}
