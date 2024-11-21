buildscript {
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:12.1.1")
    }
}

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ktlint)
}

apply {
    from("scripts/git-hooks.gradle.kts")
}

ktlint {
    version = "1.4.0"
}

afterEvaluate {
    tasks.getByName("clean").dependsOn("installGitHooks")
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
