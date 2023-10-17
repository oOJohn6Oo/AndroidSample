package io.john6.sample.fragmentcommunicate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.john6.base.util.log
import io.john6.base.util.visible
import io.john6.sample.data.ReadMeUrlList
import io.john6.sample.fragmentcommunicate.databinding.ActivityDemoFragmentCommunicateBinding
import io.john6.sample.ui.WebActivity
import kotlin.random.Random

class DemoFragment :Fragment(){
    private val mViewModel by lazy {
        ViewModelProvider(this)[DemoViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CommonScreen(
                    value = mViewModel.sharedValue.collectAsState().value,
                    showSlider = false,
                    backgroundColor = Color(mViewModel.bgdColor),
                    onShowBtnClick = this@DemoFragment::onBtnShowClicked,
                    onInfoBtnClick = this@DemoFragment::onInfoBtnClicked
                )
            }
        }
    }

    private fun onBtnShowClicked(value: Int) {
        DemoDialogFragment.show(parentFragmentManager, value, getRequestKey())
    }

    private fun onInfoBtnClicked(){
        WebActivity.show(requireContext(), ReadMeUrlList.FragmentCommunicate.fragmentUrl)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 监听来自 DialogFragment 的结果
        parentFragmentManager.setFragmentResultListener(getRequestKey(), viewLifecycleOwner){ _, bundle ->
            mViewModel.submitValue(bundle.getInt("result"))
        }
    }

    private fun getRequestKey() = tag ?: this::class.java.simpleName
}