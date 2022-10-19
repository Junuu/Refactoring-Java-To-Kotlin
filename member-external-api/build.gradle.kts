
plugins {
    java
    id("org.jetbrains.kotlin.plugin.jpa")
    id("org.asciidoctor.jvm.convert")
}

val asciidoctorExtensions by configurations.creating

dependencies {
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    //test
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")

    //encoding
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    //restdocs
    asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    //database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java")
    runtimeOnly("com.h2database:h2")


}


val snippetsDir by extra {
    file("build/generated-snippets")
}

tasks {
    asciidoctor {
        dependsOn(test)
        configurations("asciidoctorExtensions")
        inputs.dir(snippetsDir)
    }
    register<Copy>("copyDocument") {
        dependsOn(asciidoctor)
        from(file("build/docs/asciidoc/index.html"))
        into(file("src/main/resources/static/docs"))
    }

    bootJar {
        dependsOn("copyDocument")
        archiveFileName.set("boot.jar")
    }
}


