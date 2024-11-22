plugins {
    id("root.publication")
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.dokka) apply false
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

subprojects {
    pluginManager.apply("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    version = "1.4.0"
}
