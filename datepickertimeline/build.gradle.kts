plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "com.foreverrafs.datepicker"
}

apply {
    from("../scripts/publish.gradle")
}

dependencies {
    implementation(platform(libs.compose.bom))
    coreLibraryDesugaring(libs.desugaring)
    implementation(libs.androidx.core)
    testImplementation(libs.junit)
    implementation(libs.compose.ui.ui)

    // Tooling support (Previews, etc.)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.tooling.data)

    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)

    // Material Design
    implementation(libs.compose.material.material2)

    // Material design icons
    implementation(libs.compose.material.iconsextended)
    testImplementation(libs.assertJ)

    // Snapper
    implementation(libs.snapper) {
        because("This one works better than Google's version")
    }
}
