package io.john6.sample.doublestickyheader

import DemoDoubleStickyHeaderScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat

class DemoDoubleStickyHeaderActivity: ComponentActivity(){

    private val vm by viewModels<DemoVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { DemoDoubleStickyHeaderScreen() }
        println("${vm::class.java} start")
    }
}