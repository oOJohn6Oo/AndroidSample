plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("io.john6.sample.plugin.commonAndroidBuildPlugin")
}

android {
    namespace = "io.john6.sample.imageload"
}

dependencies {
    implementation(project(":core:ui"))
}