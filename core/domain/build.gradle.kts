plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {

    // `:core:domain` should not depend on the data or domain layer

    // KotlinX Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Since this module is a Kotlin JVM (Java Virtual Machine) module, when you try to add Hilt here,
    // you get the error ‘The Hilt Android Gradle plugin can only be applied to an Android project.’.
    // For this reason, `javaxInject` is implemented here
    //
    // Javax
    implementation(libs.javax.inject)

}