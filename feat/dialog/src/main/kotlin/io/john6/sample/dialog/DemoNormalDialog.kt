package io.john6.sample.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.john6.johnbase.util.getColorInt
import com.john6.johnbase.util.tint
import com.john6.johnbase.util.vdp
import io.john6.sample.dialog.databinding.DialogDemoBottomSheetBinding

class DemoNormalDialog: DialogFragment(){

    private var _mBinding: DialogDemoBottomSheetBinding? = null
    private val mBinding: DialogDemoBottomSheetBinding get() = _mBinding!!

    private var itemCount: Int = defaultItemCount

    override fun getTheme() = R.style.CustomNormalDialog

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

        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setAllCornerSizes(16.vdp)
            .build()

        val colorSurface = MaterialColors.getColor(window.context, com.google.android.material.R.attr.colorSurface, Color.WHITE)
        window.decorView.background = MaterialShapeDrawable(shapeAppearanceModel).apply {
            initializeElevationOverlay(window.context)
            fillColor = colorSurface.tint
            elevation = dialog?.window?.decorView?.elevation ?: 4.vdp
        }
        itemCount = arguments?.getInt("itemCount", 100) ?: 100
        val adapter = DemoAdapter()
        adapter.submitList(List(itemCount) { "item ${it + 1}" })
        mBinding.recyclerViewFgDdbs.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

    companion object {
        fun show(supportFragmentManager: FragmentManager, itemCount: Int) {
            DemoNormalDialog().apply {
                arguments = Bundle().apply {
                    putInt("itemCount", itemCount)
                }
                show(supportFragmentManager, "DemoNormalDialog")
            }
        }
    }
}