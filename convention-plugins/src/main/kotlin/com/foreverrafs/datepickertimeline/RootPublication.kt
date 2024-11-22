package com.foreverrafs.datepickertimeline

import io.github.gradlenexus.publishplugin.NexusPublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class RootPublication : Plugin<Project> {
    private fun getCurrentBranch(): String {
        val command = listOf(
            "git",
            "rev-parse",
            "--abbrev-ref",
            "HEAD",
        )
        val process = ProcessBuilder(command).start()

        val output = process.inputStream.bufferedReader().use { it.readText() }
        return output
    }

    private fun getLibraryVersion(): String {
        val nextReleaseVersion = "3.0.1"
        val currentBranch = getCurrentBranch()

        // develop branch is for snapshots whilst main is for releases
        val version = if (currentBranch == "develop") {
            "$nextReleaseVersion-SNAPSHOT"
        } else {
            nextReleaseVersion
        }

        println("Publishing version: $version")
        return version
    }

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.github.gradle-nexus.publish-plugin")
            allprojects {
                group = "io.github.rafsanjani"
                version = getLibraryVersion()
            }

            nexusPublishing {
                repositories {
                    sonatype {
                        val mavenCentralUsername = System.getenv("MAVEN_CENTRAL_USERNAME")
                        val mavenCentralPassword = System.getenv("MAVEN_CENTRAL_PASSWORD")

                        snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
                        nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))

                        username.set(mavenCentralUsername)
                        password.set(mavenCentralPassword)
                    }
                }
            }
        }
    }
}

private fun Project.nexusPublishing(action: NexusPublishExtension.() -> Unit) = extensions.configure(action)
