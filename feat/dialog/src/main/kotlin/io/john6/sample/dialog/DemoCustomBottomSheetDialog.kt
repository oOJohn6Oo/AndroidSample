package io.john6.sample.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import com.john6.johnbase.util.InsetsHelper
import com.john6.johnbase.util.safeDrawing
import com.john6.johnbase.util.setBottomPadding
import io.john6.sample.dialog.databinding.DialogDemoBottomSheetBinding

class DemoCustomBottomSheetDialog: BottomSheetDialogFragment() {

    private var _mBinding: DialogDemoBottomSheetBinding? = null
    private val mBinding: DialogDemoBottomSheetBinding get() = _mBinding!!

    private var itemCount: Int = defaultItemCount

    private val mInsetsHelper = InsetsHelper().apply {
        enforceNavBar = true
    }

    override fun getTheme() = R.style.CustomBottomSheetDialog

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
        itemCount = arguments?.getInt("itemCount", 100) ?: 100
        val adapter = DemoAdapter()
        adapter.submitList(List(itemCount) { "item ${it + 1}" })
        mBinding.recyclerViewFgDdbs.adapter = adapter
        val dividerColor = MaterialColors.getColor(
            window.context,
            com.google.android.material.R.attr.colorOnSurface,
            Color.WHITE
        ).withAlpha(0x1A)
        mBinding.recyclerViewFgDdbs.addItemDecoration(EdgeItemDecoration(dividerColor))

        mInsetsHelper.onInsetsChanged = {
            mBinding.recyclerViewFgDdbs.setBottomPadding(it.safeDrawing().bottom)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

    companion object {
        fun show(supportFragmentManager: FragmentManager, itemCount: Int) {
            DemoCustomBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putInt("itemCount", itemCount)
                }
                show(supportFragmentManager, "DemoCustomBottomSheetDialog")
            }
        }
    }
}
