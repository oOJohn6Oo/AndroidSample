## Dialog 重要代码说明

### 1. 获取调用方当前 value 

```kotlin
private val mValue by lazy {
    requireArguments().getInt("value")
}
```

### 2. 点击按钮后，将 value 传递给调用方

```kotlin
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
```