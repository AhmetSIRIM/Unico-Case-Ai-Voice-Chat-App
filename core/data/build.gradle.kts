plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.android.secrets.gradle.plugin)
}

android {
    namespace = "com.ahmetsirim.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "GEMINI_GENERATIVE_AI_API_KEY", "\"${project.findProperty("GEMINI_GENERATIVE_AI_API_KEY") ?: "debug_dummy_key"}\"")
            buildConfigField("String", "GEMINI_GENERATIVE_AI_MODEL_NAME", "\"${project.findProperty("GEMINI_GENERATIVE_AI_MODEL_NAME") ?: "gemini-pro"}\"")
        }

        release {
            isMinifyEnabled = false

            buildConfigField("String", "GEMINI_GENERATIVE_AI_API_KEY", "\"${project.findProperty("GEMINI_GENERATIVE_AI_API_KEY") ?: ""}\"")
            buildConfigField("String", "GEMINI_GENERATIVE_AI_MODEL_NAME", "\"${project.findProperty("GEMINI_GENERATIVE_AI_MODEL_NAME") ?: "gemini-pro"}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
}

// Specifies where Room database schema files (in JSON format) will be saved.
// These files describe the database structure (tables, columns, etc.) and are used for migrations.
// "$projectDir/schemas" points to the :core:data/schemas directory.
room { schemaDirectory("$projectDir/schemas") }

dependencies {

    // Core Project Modules
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Google Generative AI
    implementation(libs.google.generative.ai)

    // Retrofit & Moshi & OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi.converter)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging)

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)

    // Room Database
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Firebase Bill of Material (BoM) & Other Firebase Libraries
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

}

// Configuration block for handling sensitive data securely (e.g., API keys, tokens, passwords)
secrets {
    propertiesFileName = "secrets.properties" // Specifies the main file where sensitive data is stored securely.
    defaultPropertiesFileName = "local.defaults.properties" // Provides a fallback file with default values for missing keys, typically used in local or development environments.
}