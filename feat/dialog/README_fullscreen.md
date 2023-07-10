## 全屏弹窗 重要代码说明

### 1. 改变 Window 大小及允许内容延伸到边栏

```kotlin
WindowCompat.setDecorFitsSystemWindows(window, false)
window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
```

### 2. 主题设置

最重要的就下面三个属性

```xml
<item name="android:windowIsFloating">false</item>
<item name="android:statusBarColor">@android:color/transparent</item>
<item name="android:navigationBarColor">@android:color/transparent</item>
```
