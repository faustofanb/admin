package io.github.faustofan.admin

import org.babyfish.jimmer.client.EnableImplicitApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableImplicitApi
open class AdminBackendApplication

fun main(args: Array<String>) {
    runApplication<AdminBackendApplication>(*args)
}