import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("io.john6.sample.plugin.commonAndroidBuildPlugin")
}

android {
    namespace = "io.john6.sample.ui"
}

dependencies {
    api("androidx.activity:activity-compose:1.8.0")
    api("androidx.compose.ui:ui-tooling:1.5.3")
    api("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    api("androidx.compose.material:material-icons-extended:1.5.3")

    // Personal library contains common util code
    api("com.github.oOJohn6Oo.BaseAndroid:john-base:1.0.8")
    api("com.github.oOJohn6Oo.BaseAndroid:john-base-compose:1.0.8")
}