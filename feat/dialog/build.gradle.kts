plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("io.john6.sample.plugin.commonAndroidBuildPlugin")
}

android {
    namespace = "io.john6.sample.dialog"
}

dependencies {
    implementation(project(":core:ui"))
    implementation("com.google.android.material:material:1.9.0")
}