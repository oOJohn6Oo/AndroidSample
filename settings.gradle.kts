pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("plugins")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}


rootProject.name = "ComposeSample"
include(":app")
include(":feat:loadImage")
include(":feat:dialog")
include(":feat:fragmentcommunicate")
include(":core:ui")
include(":core:data")
