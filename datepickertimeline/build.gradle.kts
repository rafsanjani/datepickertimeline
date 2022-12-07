plugins {
    id("com.android.library")
    id("kotlin-android")
}

val composeBOM = "2022.12.00"

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    namespace = "com.foreverrafs.datepicker"
}
apply {
    from("../scripts/publish.gradle")
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:$composeBOM"))
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")
    implementation("androidx.core:core-ktx:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
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
    testImplementation("org.assertj:assertj-core:3.23.1")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    testImplementation("org.assertj:assertj-core:3.23.1")
    androidTestImplementation("org.assertj:assertj-core:3.23.1")
}
