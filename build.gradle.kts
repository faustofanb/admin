import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.io.spring.dependency.management)
    alias(libs.plugins.ksp)
}

group = "io.github.faustofan.admin"
version = "1.0.0"

java {
    toolchain {
        // 保持与本地环境一致：Java 21
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot BOM
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))

    // Jimmer
    implementation(libs.jimmer.starter)
    ksp(libs.jimmer.ksp)
    runtimeOnly(libs.jimmer.client.swagger)

    // Spring Web + Modulith
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.modulith)

    // 数据库
    runtimeOnly(libs.h2.database)

    // 测试
    testImplementation(libs.spring.boot.starter.test)

    // ❗重要修改：不要使用 libs.junit.platform.launcher (它带了版本号 1.12.0)
    // 直接使用字符串，不带版本号，让 Spring Boot BOM 决定版本
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// ℹ️ 移除：sourceSets { main { ... } }
// Kotlin 插件默认会自动处理 src/main/java 和 src/main/kotlin，手动写多余且可能出错

// KSP 生成目录
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

// Kotlin 编译配置
tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

// 测试任务配置
tasks.withType<Test> {
    useJUnitPlatform()

    // ❗重要修改：合并 JVM 参数
    // EnableDynamicAgentLoading: 解决 JDK 21+ Mockito 报错
    // Xmx1G: 给予足够内存防止 OOM 导致进程退出
    jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xmx1G")

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true // 出错时能在控制台看到具体日志
    }
}