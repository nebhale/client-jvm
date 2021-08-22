plugins {
    jacoco
    `java-library`
    `maven-publish`
    signing
}

dependencies {
    compileOnly("org.jetbrains:annotations:20.1.0")

    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("org.jetbrains:annotations:20.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation("org.postgresql:postgresql:42.2.23")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(false)
    }
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).links?.add("https://docs.oracle.com/en/java/javase/11/docs/api/")
}

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                groupId = "com.nebhale.bindings"
                artifactId = "client-java"
                version = "1.0.0"

                name.set("Service Bindings for Kubernetes")
                description.set("A library to access Service Binding Specification for Kubernetes conformant Service Binding Workload Projections.")
                url.set("https://github.com/nebhale/client-java")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        name.set("Ben Hale")
                        email.set("nebhale@nebhale.com")
                        url.set("https://github.com/nebhale")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/nebhale/client-java.git")
                    developerConnection.set("scm:git:https://github.com/nebhale/client-java.git")
                    url.set("https://github.com/nebhale/client-java")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")

            credentials {
                username = project.property("ossrh.username") as String
                password = project.property("ossrh.password") as String
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}
