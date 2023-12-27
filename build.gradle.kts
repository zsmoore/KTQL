import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21" apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.21"))
    }
}

group = "com.zachary-moore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}