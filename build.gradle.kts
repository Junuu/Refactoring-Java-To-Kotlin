import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("org.springframework.boot") version "2.6.7" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.21" apply false
    java
    id("org.jetbrains.kotlin.plugin.spring") version "1.6.21" apply false
    id("org.jetbrains.kotlin.plugin.jpa") version "1.6.21" apply false
    id("org.asciidoctor.jvm.convert") version "3.3.2" apply false
}

allprojects {
    group = "anthill"
    version = "0.0.1-SNAPSHOT"

    tasks.withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile>{
        kotlinOptions{
            freeCompilerArgs = listOf("-Xjst305=strict")
            jvmTarget = "11"
        }
    }
}

subprojects{
    repositories {
        mavenCentral()
    }
}

dependencies {
}

