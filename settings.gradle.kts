pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from("io.github.rafsanjani:versions:2024.11.17")
        }
    }
}

rootProject.name = "datepicker"
include(":app")
include(":datepickertimeline")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
