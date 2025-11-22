package io.github.faustofanb.admin.module.system.repository;

import io.github.faustofanb.admin.module.system.domain.entity.SysMenu;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuRepository extends JRepository<SysMenu, Long> {
    
    List<SysMenu> findByStatus(int status);
}
