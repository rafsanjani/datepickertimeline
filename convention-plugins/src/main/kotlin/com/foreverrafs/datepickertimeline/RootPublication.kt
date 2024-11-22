package com.foreverrafs.datepickertimeline

import io.github.gradlenexus.publishplugin.NexusPublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class RootPublication : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.github.gradle-nexus.publish-plugin")

            target.allprojects {
                group = "io.github.rafsanjani"
                version = "1.2.2-SNAPSHOT"
            }

            nexusPublishing {
                repositories {
                    sonatype {
                        val mavenCentralUsername = System.getenv("MAVEN_CENTRAL_USERNAME")
                        val mavenCentralPassword = System.getenv("MAVEN_CENTRAL_PASSWORD")

                        nexusUrl.set(
                            if (version.toString().endsWith("SNAPSHOT")) {
                                uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                            } else {
                                uri("https://s01.oss.sonatype.org/service/local/")
                            },
                        )

                        username.set(mavenCentralUsername)
                        password.set(mavenCentralPassword)
                    }
                }
            }
        }
    }
}

private fun Project.nexusPublishing(action: NexusPublishExtension.() -> Unit) = extensions.configure(action)
