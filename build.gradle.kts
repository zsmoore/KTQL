plugins {
    kotlin("jvm") version "1.9.21"
    id("maven-publish")
    id("signing")
    id("java-library")
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
version = "0.0.1"

publishing {
    repositories {
        maven {
            name = "mavenCentral"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = "sonatypeUsername".byProperty
                password = "sonatypePassword".byProperty
            }
        }
    }
    if (project.name != "integration") {
        publications.register<MavenPublication>("KTQLPublication") {
            from(components["java"])
        }
    }
    publications.withType<MavenPublication> {
        pom {
            setDescription("Write GraphQL with Kotlin")
            name.set("KTQL")
            url.set("https://github.com/zsmoore/KTQL")
            organization {
                name.set("com.zachary-moore")
                url.set("https://www.zachary-moore.com")
            }

            issueManagement {
                system.set("Github")
                url.set("https://github.com/zsmoore/KTQL")
            }

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://github.com/zsmoore/KTQL/blob/main/LICENSE")
                    distribution.set("repo")
                }
            }

            developers {
                developer {
                    id.set("zsmoore")
                    name.set("Zachary Moore")
                    email.set("zsmoore@zachary-moore.com")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/zsmoore/KTQL.git")
                developerConnection.set("scm:git:ssh://git@github.com:zsmoore/KTQL.git")
                url.set("https://github.com/zsmoore/KTQL")
            }
        }
    }
}

signing {
    sign(publishing.publications["KTQLPublication"])
}

val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")
tasks.withType<Sign> {
    onlyIf { isReleaseVersion }
}

val String.byProperty: String? get() = providers.gradleProperty(this).orNull