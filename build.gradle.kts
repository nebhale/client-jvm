plugins {
    jacoco
    `java-library`
    `maven-publish`
    signing
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")

    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.jetbrains:annotations:24.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.postgresql:postgresql:42.7.2")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    withJavadocJar()
    withSourcesJar()
}

tasks.named<Test>("test") {
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
                groupId = "com.nebhale"
                artifactId = "service-bindings"
                version = "1.0.0"

                name.set("Service Bindings for Kubernetes")
                description.set("A library to access Service Binding Specification for Kubernetes conformant Service Binding Workload Projections.")
                url.set("https://github.com/nebhale/client-jvm")

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
                    connection.set("scm:git:https://github.com/nebhale/client-jvm.git")
                    developerConnection.set("scm:git:https://github.com/nebhale/client-jvm.git")
                    url.set("https://github.com/nebhale/client-jvm")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")

            credentials {
                if (project.hasProperty("ossrh.username")) {
                    username = project.property("ossrh.username") as String
                }

                if (project.hasProperty("ossrh.password")) {
                    password = project.property("ossrh.password") as String
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}
