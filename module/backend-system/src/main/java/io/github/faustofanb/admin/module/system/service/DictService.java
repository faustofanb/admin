package io.github.faustofanb.admin.module.system.service;

import java.util.List;

import io.github.faustofanb.admin.module.system.domain.entity.SysDictData;

public interface DictService {

    List<SysDictData> selectDictDataByType(String dictType);

    void deleteDictTypeByIds(List<Long> ids);

    void deleteDictDataByIds(List<Long> ids);

    // Add other CRUD methods as needed
}
