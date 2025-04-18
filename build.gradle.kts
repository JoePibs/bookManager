plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("info.solidsoft.pitest") version "1.15.0"
    application
    jacoco
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest:kotest-property:5.9.1")
    // Spring MVC Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit-vintage-engine")
    }
    // Mockk pour Spring
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("io.mockk:mockk-spring:1.13.5")
    // JSON assertions
    testImplementation("com.jayway.jsonpath:json-path:2.7.0")
}

application {
    mainClass.set("bookManager.DemoApplicationKt")
}

jacoco {
    toolVersion = "0.8.11"

}

tasks.jacocoTestReport {
    dependsOn("test", "testIntegration")
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    executionData.setFrom(
        fileTree(buildDir).apply {
            include("/jacoco/*.exec")
        }
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    testLogging {
        showStandardStreams = true
    }
}

pitest {
    junit5PluginVersion.set("1.2.0")
    targetClasses.set(listOf("bookManager.domain.*"))
    targetTests.set(listOf("bookManager.*"))
    threads.set(4)
    outputFormats.set(listOf("HTML", "XML"))
    timestampedReports.set(false)
    mutationThreshold.set(80)
}

sourceSets {
    val testIntegration by creating {
        kotlin.srcDir("src/testIntegration/kotlin")
        resources.srcDir("src/testIntegration/resources")
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath   += output + compileClasspath
    }
}

configurations {
    val testIntegrationImplementation by getting {
        extendsFrom(configurations["testImplementation"])
    }
    val testIntegrationRuntimeOnly by getting {
        extendsFrom(configurations["testRuntimeOnly"])
    }
}

tasks.register<Test>("testIntegration") {
    description = "Exécute les tests d’intégration"
    group = "verification"
    testClassesDirs = sourceSets["testIntegration"].output.classesDirs
    classpath = sourceSets["testIntegration"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.check {
    dependsOn("testIntegration")
}
