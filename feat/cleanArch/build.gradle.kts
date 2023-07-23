plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    id("io.john6.sample.plugin.commonAndroidBuildPlugin")
}

android {
    namespace = "io.john6.sample.cleanarch"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":feat:cleanArch:data"))
    implementation("androidx.navigation:navigation-compose:2.6.0")
}