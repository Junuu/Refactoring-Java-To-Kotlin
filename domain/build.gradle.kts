plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  kotlin("plugin.allopen")
  kotlin("plugin.jpa")
  kotlin("kapt")
}

allOpen {
  annotation("org.springframework.stereotype.Service")
}


dependencies {
  implementation(project(":infra-rds"))
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test")

  implementation("org.mindrot:jbcrypt:0.4")
  implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.getByName("bootJar") {
  enabled = false
}
tasks.getByName("jar") {
  enabled = true
}

