package io.github.faustofanb.admin.module.system.controller;

import io.github.faustofanb.admin.common.model.Result;
import io.github.faustofanb.admin.module.system.domain.entity.SysMenu;
import io.github.faustofanb.admin.module.system.domain.entity.SysUser;
import io.github.faustofanb.admin.module.system.model.LoginBody;
import io.github.faustofanb.admin.module.system.model.LoginUser;
import io.github.faustofanb.admin.module.system.service.SysLoginService;
import io.github.faustofanb.admin.module.system.service.SysMenuService;
import io.github.faustofanb.admin.module.system.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class SysLoginController {

    private final SysLoginService loginService;
    private final SysMenuService menuService;
    private final TokenService tokenService;

    @PostMapping("/auth/login")
    public Result<Map<String, String>> login(@RequestBody LoginBody loginBody) {
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword());
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return Result.success(map);
    }

    @PostMapping("/auth/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenService.delLoginUser(token);
        }
        return Result.success();
    }

    @GetMapping("/getInfo")
    public Result<Map<String, Object>> getInfo(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        SysUser user = loginUser.getUser();
        Set<String> permissions = loginUser.getPermissions();
        
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("permissions", permissions);
        map.put("roles", user.roles()); // Assuming roles are fetched
        return Result.success(map);
    }

    @GetMapping("/getRouters")
    public Result<List<SysMenu>> getRouters(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(loginUser.getUserId());
        return Result.success(menus);
    }
}
