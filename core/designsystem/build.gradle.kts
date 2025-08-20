plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ahmetsirim.designsystem"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // Core Project Modules
    implementation(projects.core.common)
    implementation(projects.core.domain)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)

    // Compose Dependencies & Hilt Navigation Compose Dependency
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
    api(libs.androidx.ui.google.fonts)
    api(libs.androidx.compose.ui)
    api(libs.androidx.foundation)
    api(libs.hilt.navigation.compose)
    api(libs.androidx.ui.tooling)
    api(libs.androidx.ui.tooling.preview)

}