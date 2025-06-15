pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from("io.github.rafsanjani:versions:2025.06.15")
        }
    }
}

rootProject.name = "convention-plugins"
