plugins {
    kotlin("jvm")
    java
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
    id("com.apollographql.apollo3").version("4.0.0-beta.1")
}

repositories {
    mavenCentral()
}

dependencies {
    ksp(project(":processor"))
    implementation(project(":codegen"))
    implementation("com.graphql-java:graphql-java:21.3")
    implementation(project(":engine"))
    implementation("com.apollographql.apollo3:apollo-runtime")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register("genKTQLInline", JavaExec::class) {
    classpath = sourceSets["main"].runtimeClasspath
    main = "com.zachary_moore.runner.Runner"
    file(buildDir.toPath().resolve("generated/ktql/main/kotlin")).mkdirs()
    args = listOf(
        file("src/test/resources/schema.graphqls").toPath().toAbsolutePath().toString(),
        buildDir.toPath().resolve("generated/ktql/main/kotlin/com/zachary_moore/ktql/").toAbsolutePath().toString(),
        "INLINE_TRANSLATION"
    )
}

tasks.register("genKTQLFileGeneration", JavaExec::class) {
    classpath = sourceSets["main"].runtimeClasspath
    main = "com.zachary_moore.runner.Runner"
    file(buildDir.toPath().resolve("generated/ktql/main/kotlin")).mkdirs()
    args = listOf(
        file("src/test/resources/schema.graphqls").toPath().toAbsolutePath().toString(),
        buildDir.toPath().resolve("generated/ktql/main/kotlin/com/zachary_moore/ktql/file/").toAbsolutePath().toString(),
        "FILE_GENERATION"
    )
}

sourceSets {
    main {
        kotlin {
            srcDir(buildDir.toPath().resolve("generated/ktql/main/kotlin"))
        }
    }
}

apollo {
    service("service") {
        packageName.set("com.zachary_moore.integration")
        alwaysGenerateTypesMatching.add(".*")
        schemaFile.set(file("src/test/resources/schema.graphqls"))
    }
}

tasks.named("build") {
    dependsOn("genKTQLInline")
    dependsOn("genKTQLFileGeneration")
}