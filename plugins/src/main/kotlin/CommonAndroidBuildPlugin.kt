import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class CommonAndroidBuildPlugin: Plugin<Project>{
    override fun apply(project: Project) {
        project.extensions.getByType(BaseExtension::class).run {
            val isApp = this is com.android.build.gradle.AppExtension
            compileSdkVersion(34)
            defaultConfig {
                minSdk = 21
                targetSdk = 34
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                getByName("release"){
                    isMinifyEnabled = isApp
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )

                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            project.tasks.withType<KotlinCompile>{
                kotlinOptions {
                    jvmTarget = "17"
                }
            }

            buildFeatures.viewBinding = true
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = "1.4.3"
        }
    }
}