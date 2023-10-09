package io.john6.sample




val allAvailableModuleInfo by lazy {
    listOf(
        FeatureModuleInfo(
            "Load Image",
            "Best practice to load/write images from/to local storage with minimal permission",
            "loadImage",
            Util.safeGetClass("io.john6.sample.loadimage.LoadImageTest")
        ),
        FeatureModuleInfo(
            "All Dialog",
            "Best practice on implementing all kinds of dialog",
            "dialog",
            Util.safeGetClass("io.john6.sample.dialog.DialogTestActivity")
        ),
        FeatureModuleInfo(
            "Fragment Communication",
            "Best practice on demonstrating how to communicate between fragments",
            "fragmentCommunicate",
            Util.safeGetClass("io.john6.sample.fragmentcommunicate.FragmentCommunicateActivity")
        ),
        FeatureModuleInfo(
            "Clean Architecture",
            "Best practice on implementing clean architecture",
            "cleanArch",
            Util.safeGetClass("io.john6.sample.cleanarch.presentation.MainActivity")
        ),
        FeatureModuleInfo(
            "Double StickyHeader",
            "Best practice on implementing 2 StickyHeader shows at same time",
            "doubleStickyHeader",
            Util.safeGetClass("io.john6.sample.doublestickyheader.DemoDoubleStickyHeaderActivity")
        ),
    )
}

object Util {

    fun safeGetClass(clazz: String) = try {
        Class.forName(clazz)
    } catch (e: Exception) {
        e.printStackTrace()
        SampleAtt::class.java
    }
}