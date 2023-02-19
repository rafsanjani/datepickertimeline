plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
val composeBOM = "2023.01.00"

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.foreverrafs.datepickertimeline"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "11"

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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packagingOptions {
        resources {
            excludes += listOf(
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

    implementation(platform("androidx.compose:compose-bom:$composeBOM"))

    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation")

    // Material Design
    implementation("androidx.compose.material:material")

    // Material design icons
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(project(mapOf("path" to ":datepickertimeline")))

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    testImplementation("org.assertj:assertj-core:3.23.1")
    androidTestImplementation("org.assertj:assertj-core:3.23.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.godaddy.android.colorpicker:compose-color-picker-android:0.6.0")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.2")
}
