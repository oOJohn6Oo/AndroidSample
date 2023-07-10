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
    api("androidx.activity:activity-compose:1.7.2")
    api("androidx.compose.ui:ui-tooling:1.4.3")
    api("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    api("androidx.compose.material:material-icons-extended:1.4.3")

    // Personal library contains common util code
    api("com.github.oOJohn6Oo.BaseAndroid:john-base:v1.0.3")
    api("com.github.oOJohn6Oo.BaseAndroid:john-base-compose:v1.0.3")

    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.1.5")
    androidTestApi("androidx.test.espresso:espresso-core:3.5.1")
}