pluginManagement {
    includeBuild("convention-plugins")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

    versionCatalogs {
        create("libs") {
            from("io.github.rafsanjani:versions:2025.07.27")
        }
    }
}

rootProject.name = "datepicker"
include(":app")
include(":datepickertimeline")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
