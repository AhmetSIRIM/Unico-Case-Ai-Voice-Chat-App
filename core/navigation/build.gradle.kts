plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.ahmetsirim.navigation"
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
    implementation(projects.core.designsystem)

    // Feature Project Modules
    //
    // The reason why Feature Project Modules
    // are integrated with `api` is to give
    // the app module access to the feature modules.
    api(projects.feature.chat)
    api(projects.feature.history)
    api(projects.feature.settings)

    // Navigation Component
    implementation(libs.navigation.common)
    implementation(libs.navigation.compose)

    // KotlinX Serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.compose.material3)
    implementation(libs.kotlinx.coroutines.core)

}