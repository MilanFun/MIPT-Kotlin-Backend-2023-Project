import org.codehaus.groovy.tools.shell.util.Logger.io

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.5"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-netty:1.6.5")
    implementation("io.ktor:ktor-server-core:1.6.7-eap-282")
    implementation("io.ktor:ktor-gson:1.6.5")
    implementation("org.ktorm:ktorm-core:3.2.0")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json:1.6.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}