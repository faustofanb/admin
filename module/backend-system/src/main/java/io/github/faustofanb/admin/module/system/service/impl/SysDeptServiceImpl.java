package io.github.faustofanb.admin.module.system.service.impl;

import io.github.faustofanb.admin.module.system.domain.entity.SysDept;
import io.github.faustofanb.admin.module.system.domain.entity.SysDeptDraft;
import io.github.faustofanb.admin.module.system.repository.SysDeptRepository;
import io.github.faustofanb.admin.module.system.repository.SysUserRepository;
import io.github.faustofanb.admin.module.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl implements SysDeptService {

    private final SysDeptRepository deptRepository;
    private final SysUserRepository userRepository;

    @Override
    public List<SysDept> selectDeptTree(SysDept dept) {
        // Fetch all depts (filtering can be added later)
        List<SysDept> depts = deptRepository.findAll();
        return buildDeptTree(depts);
    }

    @Override
    public void checkDeptDataScope(Long deptId) {
        // TODO: Implement data scope check
    }

    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return deptRepository.countByParentId(deptId) > 0;
    }

    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return userRepository.countByDeptId(deptId) > 0;
    }

    private List<SysDept> buildDeptTree(List<SysDept> depts) {
        if (depts.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, List<SysDept>> childrenMap = depts.stream()
                .filter(d -> d.parentId() != null)
                .collect(Collectors.groupingBy(SysDept::parentId));

        Set<Long> deptIds = depts.stream().map(SysDept::id).collect(Collectors.toSet());
        List<SysDept> roots = depts.stream()
                .filter(d -> d.parentId() == null || !deptIds.contains(d.parentId()))
                .toList();

        return roots.stream()
                .map(root -> buildTreeRecursive(root, childrenMap))
                .toList();
    }

    private SysDept buildTreeRecursive(SysDept current, Map<Long, List<SysDept>> childrenMap) {
        List<SysDept> children = childrenMap.get(current.id());
        if (children == null || children.isEmpty()) {
            return current;
        }

        List<SysDept> builtChildren = children.stream()
                .map(child -> buildTreeRecursive(child, childrenMap))
                .toList();

        return SysDeptDraft.$.produce(current, draft -> {
            draft.setChildren(builtChildren);
        });
    }
}
