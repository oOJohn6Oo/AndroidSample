plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.john6.sample.plugin.commonAndroidBuildPlugin")
}

android {
    namespace = "io.john6.sample"

    defaultConfig {
        applicationId = "io.john6.sample"
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    runtimeOnly(project(":feat:loadImage"))
    runtimeOnly(project(":feat:dialog"))
    runtimeOnly(project(":feat:fragmentCommunicate"))
    runtimeOnly(project(":feat:cleanArch"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
}