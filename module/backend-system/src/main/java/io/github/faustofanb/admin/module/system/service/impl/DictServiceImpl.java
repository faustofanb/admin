package io.github.faustofanb.admin.module.system.service.impl;

import io.github.faustofanb.admin.module.system.domain.entity.SysDictData;
import io.github.faustofanb.admin.module.system.repository.SysDictDataRepository;
import io.github.faustofanb.admin.module.system.repository.SysDictTypeRepository;
import io.github.faustofanb.admin.module.system.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final SysDictTypeRepository dictTypeRepository;
    private final SysDictDataRepository dictDataRepository;

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        return dictDataRepository.findByDictType_Type(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictTypeByIds(List<Long> ids) {
        dictTypeRepository.deleteAllById(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictDataByIds(List<Long> ids) {
        dictDataRepository.deleteAllById(ids);
    }
}
