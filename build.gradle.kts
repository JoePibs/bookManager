plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.junit.platform.gradle.plugin") version "1.8.2"
    application
}

group = "com.example.demo"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest:kotest-property:5.9.1")
    testImplementation("io.mockk:mockk:1.13.8")

    // Spring test (optionnel)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

application {
    mainClass.set("bookManager.DemoApplicationKt")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.withType<Test> {
    testLogging {
        showStandardStreams = true
    }
}