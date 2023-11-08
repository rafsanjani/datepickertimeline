plugins {
    id("com.android.library")
    id("kotlin-android")
}

val composeBOM = "2023.10.01"

android {
    compileSdk = 34

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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
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
    implementation(platform("androidx.compose:compose-bom:$composeBOM"))
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("androidx.core:core-ktx:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.compose.ui:ui")

    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling")

    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation")

    // Material Design
    implementation("androidx.compose.material:material")

    // Material design icons
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    testImplementation("org.assertj:assertj-core:3.24.2")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    androidTestImplementation("org.assertj:assertj-core:3.24.2")

    // Snapper
    implementation("dev.chrisbanes.snapper:snapper:0.3.0") {
        because("This one works better than Google's version")
    }
}
