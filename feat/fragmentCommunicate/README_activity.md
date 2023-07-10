## Activity 重要代码说明

### 1. 设置UI

```kotlin
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
```
### 2. 监听 Fragment 传递的数据

```kotlin
 supportFragmentManager.setFragmentResultListener(DemoDialogFragment.TAG, this) { _, bundle ->
     mViewModel.submitValue(bundle.getInt("result"))
 }
```