plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.21"))
    }
}



group = "com.zachary-moore"
version = "1.0-SNAPSHOT"