package io.john6.sample.dialog

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import io.john6.base.util.vdp
import kotlin.math.roundToInt

class DemoAdapter :
    ListAdapter<String, RecyclerView.ViewHolder>(CommonStringItemCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(16.vdp.roundToInt())
            setTextColor(
                MaterialColors.getColor(
                    parent.context,
                    com.google.android.material.R.attr.colorOnBackground,
                    Color.WHITE
                )
            )
        }) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tv = holder.itemView as TextView
        tv.text = getItem(position)
        val desireColor =
            tv.textColors.defaultColor.withAlpha(if (position % 2 == 0) 0.05f else 0f)
        tv.setBackgroundColor(desireColor)
    }
}

class CommonStringItemCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}