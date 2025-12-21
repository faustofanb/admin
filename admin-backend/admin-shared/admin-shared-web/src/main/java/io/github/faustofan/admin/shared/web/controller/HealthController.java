package io.github.faustofan.admin.shared.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * 健康检查端点
     *
     * @return 返回字符串 "OK" 表示服务正常运行
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
