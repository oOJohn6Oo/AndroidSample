package io.john6.sample.dialog

import android.graphics.Color
import androidx.annotation.FloatRange
import androidx.annotation.IntRange

const val defaultItemCount = 50

fun Int.withAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
    return this.withAlpha((alpha * 255).toInt())
}

fun Int.withAlpha(@IntRange(from = 0L, to = 255L) alpha: Int): Int {
    return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}