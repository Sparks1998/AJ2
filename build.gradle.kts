import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.kotlinJpa)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

group = "com.aj2"
version = "0.0.1-SNAPSHOT"
description = "AJ2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.actuator)
    runtimeOnly(libs.spring.boot.devtools)

    // Security
    implementation(libs.spring.security.oauth2.resource.server)
    implementation(libs.spring.security.oauth2.jose)
    implementation(libs.spring.security.oauth2.authorization.server)

    // Database / migrations
    runtimeOnly(libs.postgresql)
    implementation(libs.liquibase.core)
    implementation(libs.spring.boot.starter.liquibase)
    testRuntimeOnly(libs.h2database)

    // MapStruct
    implementation(libs.mapstruct)
    kapt(libs.mapstruct.processor)

    // Kotlin
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.module.kotlin)

    // OpenAPI / Swagger
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation(libs.swagger.annotations)

    // Push notifications
    implementation(libs.firebase.admin)

    // Tests
    testImplementation(libs.spring.boot.starter.test)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(kotlin("test"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict" , "-Xannotation-default-target=param-property")
        jvmTarget = JvmTarget.JVM_26
    }
    jvmToolchain(26)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.withType<JavaCompile> {
    targetCompatibility = "26"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
