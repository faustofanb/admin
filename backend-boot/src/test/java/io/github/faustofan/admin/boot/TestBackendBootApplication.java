package io.github.faustofan.admin.boot;

import org.springframework.boot.SpringApplication;

public class TestBackendBootApplication {

    public static void main(String[] args) {
        SpringApplication.from(BackendBootApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
