package io.john6.sample.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import io.john6.base.util.log
import io.john6.base.util.safeDrawing
import io.john6.sample.dialog.databinding.DialogDemoBottomSheetBinding

class DemoModalBottomSheetDialog : BottomSheetDialogFragment() {

    private var _mBinding: DialogDemoBottomSheetBinding? = null
    private val mBinding: DialogDemoBottomSheetBinding get() = _mBinding!!

    private var itemCount: Int = defaultItemCount

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
        itemCount = arguments?.getInt("itemCount", 100) ?: 100
        val adapter = DemoAdapter()
        adapter.submitList(List(itemCount) { "item ${it + 1}" })
        mBinding.recyclerViewFgDdbs.adapter = adapter

        val dividerColor = MaterialColors.getColor(
            dialog!!.context,
            com.google.android.material.R.attr.colorOnSurface,
            Color.WHITE
        ).withAlpha(0x1A)

        val container = dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.container)
        container?.also {
            ViewCompat.setOnApplyWindowInsetsListener(it){v, insets ->
                insets.safeDrawing().apply {
                    this.toString().log()
                    v.setPaddingRelative(
                        left,
                        v.paddingTop,
                        right,
                        v.paddingBottom
                    )
                }
                insets
            }
        }

        mBinding.recyclerViewFgDdbs.addItemDecoration(EdgeItemDecoration(dividerColor))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

    companion object {
        fun show(supportFragmentManager: FragmentManager, itemCount: Int) {
            DemoModalBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putInt("itemCount", itemCount)
                }
                show(supportFragmentManager, "DemoModalBottomSheetDialog")
            }
        }
    }
}
