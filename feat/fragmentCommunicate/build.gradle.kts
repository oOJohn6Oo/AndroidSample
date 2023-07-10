plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("io.john6.sample.plugin.commonAndroidBuildPlugin")
}

android {
    namespace = "io.john6.sample.fragmentcommunicate"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
}