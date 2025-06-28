plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.kayukin"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("software.amazon.awssdk:s3:2.26.16")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val clientDir = file("src/main/client")
val pnpmCmd = if (System.getProperty("os.name").startsWith("Windows")) "pnpm.cmd" else "pnpm"

val pnpmInstall by tasks.registering(Exec::class) {
    workingDir = clientDir
    commandLine(pnpmCmd, "install")
}

val pnpmBuild by tasks.registering(Exec::class) {
    dependsOn(pnpmInstall)
    workingDir = clientDir
    commandLine(pnpmCmd, "build")
}

tasks.named("processResources") {
    dependsOn(pnpmBuild)
}