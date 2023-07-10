package io.john6.sample.fragmentcommunicate

import android.graphics.LightingColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.john6.johnbase.util.getColorInt
import io.john6.sample.data.ReadMeUrlList
import io.john6.sample.ui.WebActivity

class DemoDialogFragment : DialogFragment() {
    private val mValue by lazy {
        requireArguments().getInt("value")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CommonScreen(
                    value = mValue,
                    showSlider = true,
                    iconOfShowBtn = Icons.Default.Done,
                    onInfoBtnClick = this@DemoDialogFragment::onInfoBtnClicked,
                    onShowBtnClick = this@DemoDialogFragment::onBtnOKClicked,
                )
            }
        }
    }

    private fun onInfoBtnClicked() {
        WebActivity.show(requireContext(), ReadMeUrlList.FragmentCommunicate.dialogUrl)
    }

    override fun getTheme() = androidx.appcompat.R.style.Theme_AppCompat_DayNight_Dialog_Alert

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // adapt dark-mode
        val color = requireContext().getColorInt(android.R.attr.colorBackground)
        dialog?.window?.decorView?.background?.colorFilter =
            LightingColorFilter((0xFF000000).toInt(), color)
    }

    /**
     * Triggered when OK button clicked
     */
    private fun onBtnOKClicked(value: Int) {
        val bundle = Bundle().apply {
            putInt("result", value)
        }
        parentFragmentManager.setFragmentResult(tag ?: TAG, bundle)
        dismiss()
    }

    companion object {
        const val TAG = "DemoDialogFragment"

        /**
         * DemoDialogFragment 通用显示方法
         *
         * @param fm FragmentManager
         * @param value 用于初始化 Slider 的值
         * @param tag 用于标识 Fragment
         */
        fun show(fm: FragmentManager, value: Int, tag: String = TAG) {
            DemoDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt("value", value)
                }
                show(fm, tag)
            }
        }
    }
}