plugins {
    id("org.jetbrains.kotlin.plugin.jpa")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java")
    runtimeOnly("com.h2database:h2")
}

tasks.getByName("bootJar") {
    enabled = false
}
tasks.getByName("jar") {
    enabled = true
}


