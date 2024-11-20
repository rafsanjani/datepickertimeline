plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

android {
    compileSdk = 35

    defaultConfig {
        applicationId = "com.foreverrafs.datepickertimeline"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "0.7.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"

        freeCompilerArgs =
            freeCompilerArgs + "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        freeCompilerArgs =
            freeCompilerArgs + "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi"

        freeCompilerArgs =
            freeCompilerArgs + "-Xopt-in=androidx.compose.ui.graphics.ExperimentalGraphicsApi"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes +=
                listOf(
                    "/META-INF/{AL2.0,LGPL2.1}",
                )
        }
    }

    namespace = "com.foreverrafs.datepickertimeline"

    testOptions {
        animationsDisabled = true
    }
}

dependencies {
    implementation(projects.datepickertimeline)
    implementation(platform(libs.compose.bom))

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

    // UI Tests
    androidTestImplementation(libs.compose.ui.test.unit)
    androidTestImplementation(libs.compose.ui.test.junit)
    implementation(libs.kotlin.datetime)

    implementation("com.godaddy.android.colorpicker:compose-color-picker-android:0.7.0")
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.google.material)
    implementation(libs.compose.ui.ui)
    coreLibraryDesugaring(libs.desugaring)
    implementation(libs.compose.material.material2)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)
}
