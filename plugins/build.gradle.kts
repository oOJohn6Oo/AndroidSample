import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    //添加Gradle相关的API，否则无法自定义Plugin和Task
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    compileOnly("com.android.tools.build:gradle:8.1.2")
}

gradlePlugin {
    plugins {
        create("commonAndroidBuildPlugin") {
            //添加插件
            id = "io.john6.sample.plugin.commonAndroidBuildPlugin"
            //在根目录创建类 VersionPlugin 继承 Plugin<Project>
            implementationClass = "CommonAndroidBuildPlugin"
        }

    }
}