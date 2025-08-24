plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.android.secrets.gradle.plugin)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.ahmetsirim.data"
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

    // Room Database
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Firebase Bill of Material (BoM) & Other Firebase Libraries
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    // Other
    implementation(libs.identity.jvm)
}

// Configuration block for handling sensitive data securely (e.g., API keys, tokens, passwords)
secrets {
    // Specifies the main file where sensitive data is stored securely.
    propertiesFileName = "secrets.properties"
    // Provides a fallback file with default values for missing keys,
    // typically used in local or development environments.
    defaultPropertiesFileName = "local.defaults.properties"
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    android.set(true)
    ignoreFailures.set(false)
    outputToConsole.set(true)
}
