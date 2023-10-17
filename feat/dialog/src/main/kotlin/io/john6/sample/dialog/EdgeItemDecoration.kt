package io.john6.sample.dialog

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import io.john6.base.util.vdp

/**
 * A [RecyclerView.ItemDecoration] that draws a line at top and bottom of the [RecyclerView].
 */
class EdgeItemDecoration(@ColorInt dividerColor: Int) : RecyclerView.ItemDecoration() {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dividerColor
        strokeWidth = 1.vdp
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (parent.canScrollVertically(-1)) {
            // draw a line at top
            c.drawLine(0f, 0f, parent.width.toFloat(), 0f, mPaint)
        }
        if (parent.canScrollVertically(1)) {
            // draw a line at bottom
            c.drawLine(
                0f,
                parent.height.toFloat(),
                parent.width.toFloat(),
                parent.height.toFloat(),
                mPaint
            )
        }
    }
}