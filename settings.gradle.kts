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
include(":core:ui")
include(":core:data")
include(":feat:loadImage")
include(":feat:dialog")
include(":feat:fragmentCommunicate")
include(":feat:cleanArch")
include(":feat:cleanArch:data")
