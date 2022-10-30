dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    //encoding
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
}
tasks.getByName("bootJar") {
    enabled = false
}
tasks.getByName("jar") {
    enabled = true
}