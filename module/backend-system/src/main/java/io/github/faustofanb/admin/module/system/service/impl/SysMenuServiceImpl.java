package io.github.faustofanb.admin.module.system.service.impl;

import io.github.faustofanb.admin.module.system.domain.entity.*;
import io.github.faustofanb.admin.module.system.repository.SysMenuRepository;
import io.github.faustofanb.admin.module.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuRepository menuRepository;
    private final JSqlClient sqlClient;

    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus;
        if (isSuperAdmin(userId)) {
            menus = menuRepository.findAll();
        } else {
            SysMenuTable t = SysMenuTable.$;
            menus = sqlClient.createQuery(t)
                    .where(
                            t.roles(role -> role.users(user -> user.id().eq(userId)))
                    )
                    .select(t)
                    .distinct()
                    .execute();
        }
        return buildMenuTree(menus);
    }

    @Override
    public Set<String> selectPermsByUserId(Long userId) {
        if (isSuperAdmin(userId)) {
            return Set.of("*:*:*");
        }
        SysMenuTable t = SysMenuTable.$;
        List<String> perms = sqlClient.createQuery(t)
                .where(
                        t.roles(role -> role.users(user -> user.id().eq(userId))),
                        t.status().eq(0),
                        t.perms().isNotNull()
                )
                .select(t.perms())
                .distinct()
                .execute();
        return new HashSet<>(perms);
    }


    private boolean isSuperAdmin(Long userId) {
        return userId != null && userId == 1L;
    }

    private List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        if (menus.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Group by parentId
        Map<Long, List<SysMenu>> childrenMap = menus.stream()
                .filter(m -> m.parentId() != null)
                .collect(Collectors.groupingBy(SysMenu::parentId));
        
        // Find roots (parentId is null or parentId not in the list of menu ids)
        Set<Long> menuIds = menus.stream().map(SysMenu::id).collect(Collectors.toSet());
        List<SysMenu> roots = menus.stream()
                .filter(m -> m.parentId() == null || !menuIds.contains(m.parentId()))
                .toList();

        return roots.stream()
                .map(root -> buildTreeRecursive(root, childrenMap))
                .toList();
    }

    private SysMenu buildTreeRecursive(SysMenu current, Map<Long, List<SysMenu>> childrenMap) {
        List<SysMenu> children = childrenMap.get(current.id());
        if (children == null || children.isEmpty()) {
            return current;
        }
        
        List<SysMenu> builtChildren = children.stream()
                .map(child -> buildTreeRecursive(child, childrenMap))
                .toList();

        return SysMenuDraft.$.produce(current, draft -> {
            draft.setChildren(builtChildren);
        });
    }
}
