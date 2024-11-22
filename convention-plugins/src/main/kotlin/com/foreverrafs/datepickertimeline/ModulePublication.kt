package com.foreverrafs.datepickertimeline

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.tasks.Delete
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension

class ModulePublication : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins()
            val dokkaOutputDir = layout.buildDirectory.dir("dokka").get().asFile

            val deleteDokkaOutputDir = tasks.register<Delete>("deleteDokkaOutputDirectory") {
                delete(dokkaOutputDir)
            }

            val javadocJar by tasks.register<Jar>("javadocJar") {
                dependsOn(deleteDokkaOutputDir, tasks.getByName("dokkaHtml"))
                archiveClassifier.set("javadoc")
                from(dokkaOutputDir)
            }

            publishing {
                // Configure all publications
                publications.withType<MavenPublication> {
                    val mavenPublication = this as? MavenPublication

                    artifact(javadocJar)

                    mavenPublication?.artifactId =
                        "datepickertimeline${
                            "-$name".takeUnless { "kotlinMultiplatform" in name }.orEmpty()
                        }".removeSuffix("Release")

                    pom {
                        name.set("Datepicker Timeline")
                        description.set("A linear date picker for jetpack compose multiplatform")
                        url.set("https://github.com/rafsanjani/datepickertimeline")

                        licenses {
                            license {
                                name.set("MIT")
                                url.set("https://opensource.org/licenses/mit")
                            }
                        }
                        issueManagement {
                            system.set("Github")
                            url.set("https://github.com/rafsanjani/datepickertimeline/issues")
                        }
                        scm {
                            connection.set("https://github.com/rafsanjani/datepickertimeline.git")
                            url.set("https://github.com/rafsanjani/datepickertimeline.git")
                        }
                        developers {
                            developer {
                                id.set("RafsanjaniAziz")
                                name.set("Rafsanjani Abdul-Aziz")
                                email.set("foreverrafs@gmail.com")
                            }
                        }
                    }
                }
            }

            signing {
                sign(publications)
            }

            tasks.withType(AbstractPublishToMaven::class.java).configureEach {
                dependsOn(tasks.withType(Sign::class.java))
            }
        }
    }
}

private fun Project.applyPlugins() {
    pluginManager.apply("signing")
    pluginManager.apply("org.jetbrains.dokka")
    pluginManager.apply("maven-publish")
}

private fun Project.signing(action: SigningExtension.() -> Unit) = extensions.configure(action)
private fun Project.publishing(action: PublishingExtension.() -> Unit) = extensions.configure(action)

private val SigningExtension.publications: PublicationContainer
    get() = project.extensions.getByType<PublishingExtension>().publications
