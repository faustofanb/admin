package io.github.faustofan.admin

import org.babyfish.jimmer.client.EnableImplicitApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

@SpringBootApplication
@EnableImplicitApi
@EnableSpringDataWebSupport
open class AdminBackendApplication

fun main(args: Array<String>) {
    runApplication<AdminBackendApplication>(*args)
}