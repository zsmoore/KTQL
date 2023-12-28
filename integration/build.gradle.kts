plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}

repositories {
    mavenCentral()
}

dependencies {
    ksp(project(":processor"))
    implementation(project(":codegen"))
    implementation("com.graphql-java:graphql-java:21.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}