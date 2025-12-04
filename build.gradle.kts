import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// =========================================================================
// 1. 版本定义 (集中管理，替代 libs.versions.toml)
// =========================================================================
val kotlinVersion = "2.1.20"
val kspVersion = "2.1.20-2.0.0" // 需确保与 Kotlin 版本匹配
val springBootVersion = "4.0.0"
val springModulithVersion = "2.0.0"
val springCloudVersion = "2024.0.0" // 对应 Boot 4 的 Cloud 版本
val jimmerVersion = "0.9.116"
val springDocVersion = "3.0.0"
val redissonVersion = "3.52.0"
val powerJobVersion = "5.1.2"
val rocketMqVersion = "2.3.4"
val resilience4jVersion = "2.3.0"
val minioVersion = "8.6.0"

plugins {
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "2.1.20"
    id("com.google.devtools.ksp") version "2.1.20-2.0.0"
}

group = "io.github.faustofan.admin"
version = "1.0.0"

// =========================================================================
// 2. 编译器与工具链设置 (JDK 25 + Kotlin 2.1)
// =========================================================================
java {
    toolchain {
        // 既然是2025年，直接上 JDK 25
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// 解决 Jimmer 生成代码在 IDEA 中的识别问题
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        // 目标字节码版本
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        // 开启严格模式和上下文接收器
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xcontext-receivers")
    }
}

// =========================================================================
// 3. 依赖管理
// =========================================================================
repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:$springModulithVersion")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

dependencies {
    // --- 核心 Web 与 容器 ---
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // --- Kotlin 核心 ---
    implementation("tools.jackson.module:jackson-module-kotlin:3.0.3")
    // 时间模块：解决 LocalDateTime 转成 "2025-..." 字符串
    implementation("tools.jackson.datatype:jackson-datatype-jsr310:3.0.0-rc2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // --- 安全与网关 (Gateway Module) ---
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    // --- 限流熔断
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-rxjava3:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-reactor:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-spring6:$resilience4jVersion")

    // --- 架构与文档 (Modulith + SpringDoc) ---
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    runtimeOnly("org.springframework.modulith:spring-modulith-actuator")
    runtimeOnly("org.springframework.modulith:spring-modulith-observability")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    // --- 数据持久化 (Jimmer + DB) ---
    implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:$jimmerVersion")
    ksp("org.babyfish.jimmer:jimmer-ksp:$jimmerVersion") // KSP 代码生成
    runtimeOnly("org.babyfish.jimmer:jimmer-client-swagger:$jimmerVersion") // TypeScript/OpenAPI生成支持

    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    runtimeOnly("org.postgresql:postgresql") // 生产用 PG
    runtimeOnly("com.h2database:h2")         // 开发/测试用 H2

    // --- 缓存与中间件 (Infra) ---
    // Redis (Redisson)
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.redisson:redisson:${redissonVersion}")
    // 本地缓存 (Caffeine)
    implementation("com.github.ben-manes.caffeine:caffeine")
    // 消息队列 (RocketMQ)
    implementation("org.apache.rocketmq:rocketmq-spring-boot-starter:$rocketMqVersion")
    // 对象存储 (MinIO)
    implementation("io.minio:minio:$minioVersion")
    // 分布式调度 (PowerJob Worker)
    implementation("tech.powerjob:powerjob-worker-spring-boot-starter:$powerJobVersion")

    // --- 可观测性 (Tracing) ---
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")

    // --- 开发体验 ---
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    ksp("org.springframework.boot:spring-boot-configuration-processor")

    // --- 测试套件 ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Modulith 架构测试
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")

    // Testcontainers (集成测试)
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql:1.21.3")

    // 关键修复：由 BOM 管理版本，不要指定版本号
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// =========================================================================
// 4. 测试任务配置
// =========================================================================
tasks.withType<Test> {
    useJUnitPlatform()

    // 针对 JDK 21+ 的动态代理加载问题修复，并给予足够内存
    jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xmx2G")

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}