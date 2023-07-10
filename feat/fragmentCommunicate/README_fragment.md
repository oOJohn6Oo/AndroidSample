## Fragment 重要代码说明


### 1. 监听来自 DialogFragment 的结果

```kotlin
// 监听来自 DialogFragment 的结果
parentFragmentManager.setFragmentResultListener(getRequestKey(), viewLifecycleOwner){ _, bundle ->
    mViewModel.submitValue(bundle.getInt("result"))
}
```

```kotlin
private fun getRequestKey() = tag ?: this::class.java.simpleName
```