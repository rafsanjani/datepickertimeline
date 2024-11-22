plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("io.github.gradle-nexus.publish-plugin:io.github.gradle-nexus.publish-plugin.gradle.plugin:2.0.0")
}

gradlePlugin {
    val rootPackageName = "com.foreverrafs.datepickertimeline"

    plugins {
        register("ModulePublicationConventionPlugin") {
            id = "module.publication"
            implementationClass = "$rootPackageName.ModulePublication"
        }
        register("RootPublicationConventionPlugin") {
            id = "root.publication"
            implementationClass = "$rootPackageName.RootPublication"
        }
    }
}
