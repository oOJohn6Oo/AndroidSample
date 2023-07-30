package io.john6.sample.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.john6.johnbase.util.InsetsHelper
import com.john6.johnbase.util.log
import com.john6.johnbase.util.safeDrawing
import com.john6.johnbase.util.tint
import com.john6.johnbase.util.vdp
import io.john6.sample.dialog.databinding.DialogDemoBottomSheetBinding

class DemoFullScreenDialog : AppCompatDialogFragment() {
    private var _mBinding: DialogDemoBottomSheetBinding? = null
    private val mBinding: DialogDemoBottomSheetBinding get() = _mBinding!!

    private var itemCount: Int = defaultItemCount
    private val mInsetsHelper = InsetsHelper().apply {
        enforceNavBar = true
    }

    override fun getTheme() = R.style.CustomFullScreenDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mInsetsHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = DialogDemoBottomSheetBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window = dialog?.window ?: return

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val colorSurface = MaterialColors.getColor(
            window.context,
            com.google.android.material.R.attr.colorSurface,
            Color.WHITE
        )
        window.decorView.background = MaterialShapeDrawable().apply {
            fillColor = colorSurface.tint
        }
        window.decorView.setPadding(0,0,0,0)
        itemCount = arguments?.getInt("itemCount", 100) ?: 100
        val adapter = DemoAdapter()
        adapter.submitList(List(itemCount) { "item ${it + 1}" })
        mBinding.recyclerViewFgDdbs.adapter = adapter

        mInsetsHelper.onInsetsChanged = {
            it.safeDrawing().apply {
                mBinding.recyclerViewFgDdbs.setPaddingRelative(left, top, right, bottom)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

    companion object {
        fun show(supportFragmentManager: FragmentManager, itemCount: Int) {
            DemoFullScreenDialog().apply {
                arguments = Bundle().apply {
                    putInt("itemCount", itemCount)
                }
                show(supportFragmentManager, "DemoFullScreenDialog")
            }
        }
    }
}