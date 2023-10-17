package io.john6.sample.fragmentcommunicate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import io.john6.base.util.isNightModeNow
import io.john6.sample.data.ReadMeUrlList
import io.john6.sample.fragmentcommunicate.databinding.ActivityDemoFragmentCommunicateBinding
import io.john6.sample.ui.WebActivity

/**
 * Main Activity of this module
 *
 * UI consists of 2 fragments and a CommonScreen
 */
class FragmentCommunicateActivity : AppCompatActivity() {
    private var _mBinding: ActivityDemoFragmentCommunicateBinding? = null
    private val mBinding get() = _mBinding!!

    private val mViewModel: DemoViewModel by lazy {
        DemoViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            val isDarkMode = resources.configuration.isNightModeNow
            isAppearanceLightStatusBars = !isDarkMode
            isAppearanceLightNavigationBars = !isDarkMode
        }

        _mBinding = ActivityDemoFragmentCommunicateBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // 底部添加一个 CommonScreen
        mBinding.fragmentDemo.setContent {
            CommonScreen(
                value = mViewModel.sharedValue.collectAsState().value,
                showSlider = false,
                onShowBtnClick = this::onShowDialog,
                onInfoBtnClick = this::onShowInfo,
            )
        }

        // 上半部分添加两个 DemoFragment，tag 分别设置为 "fragment A" 和 "fragment B"
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_demo_1, DemoFragment(), "fragment A")
            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_demo_2, DemoFragment(), "fragment B")
            .commit()



        supportFragmentManager.setFragmentResultListener(DemoDialogFragment.TAG, this) { _, bundle ->
            mViewModel.submitValue(bundle.getInt("result"))
        }
    }

    private fun onShowDialog(value: Int) {
        DemoDialogFragment.show(supportFragmentManager, value)
    }

    private fun onShowInfo() {
        WebActivity.show(this, ReadMeUrlList.FragmentCommunicate.mainActivityUrl)
    }

}