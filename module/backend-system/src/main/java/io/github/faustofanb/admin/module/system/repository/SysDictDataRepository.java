package io.github.faustofanb.admin.module.system.repository;

import io.github.faustofanb.admin.module.system.domain.entity.SysDictData;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictDataRepository extends JRepository<SysDictData, Long> {
    
    List<SysDictData> findByDictType_Type(String dictType);
}
