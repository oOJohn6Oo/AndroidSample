## 默认底部弹窗 重要代码说明

### 1. 主题设置

有两种方式指定主题

* 在 Activity 主题中指定所有在此 Activity 中的弹窗都使用此主题
```xml
<style name="CustomAtt" parent="Theme.John.BaseTheme.DayNight.AppTheme">
    <item name="bottomSheetDialogTheme">@style/ModalBottomSheetDialog</item>
</style>
```

* 在 Dialog 中重写 getTheme 手动指定

```kotlin
override fun getTheme() = R.style.xxx
```

### 2. BottomSheet 样式设置

```xml
<style name="BaseModalBottomSheetDialog" parent="@style/Theme.MaterialComponents.BottomSheetDialog">
    <item name="bottomSheetStyle">@style/CustomBottomSheet</item>
<!--    启用此属性后，自动调用 WindowCompat#setDecorFitsSystemWindows 方法，其他特性也依赖此属性-->
    <item name="enableEdgeToEdge">true</item>
<!--    特别重要, 如果值为 true 的话无法对边栏做更多定制-->
    <item name="android:windowIsFloating">false</item>
    <!--        指定底部留出一定空间-->
    <item name="paddingBottomSystemWindowInsets">true</item>
    <!--        定制系统边栏颜色及遮罩层-->
    <item name="android:navigationBarColor">@android:color/transparent</item>
    <item name="android:enforceNavigationBarContrast" tools:targetApi="q">false</item>
    <item name="android:enforceStatusBarContrast" tools:targetApi="q">false</item>
    <item name="android:navigationBarDividerColor" tools:targetApi="o_mr1">
        @android:color/transparent
    </item>
    <!--        定制 BottomSheetDragHandleView 的样式, Material 2 的 Workaround-->
    <item name="bottomSheetDragHandleStyle">
        @style/Widget.MaterialComponents.BottomSheet.DragHandle
    </item>
</style>
```
父主题中已设置背景色为透明，此外默认弹出动画也已设置，无需再次设置

```xml
<style name="Theme.MaterialComponents.BottomSheetDialog" parent="Theme.MaterialComponents.Dialog">
    <item name="android:windowBackground">@android:color/transparent</item>
    <item name="android:windowAnimationStyle">@style/Animation.MaterialComponents.BottomSheetDialog</item>
    <item name="bottomSheetStyle">@style/Widget.MaterialComponents.BottomSheet.Modal</item>
</style>
```


```xml
<!--    指定 BottomSheetDragHandleView 的属性-->
<style name="Widget.MaterialComponents.BottomSheet.DragHandle" parent="Widget.Material3.BottomSheet.DragHandle">
    <!--        指定背景色-->
    <item name="tint">?attr/colorOnBackground</item>
    <item name="android:alpha">0.3</item>
    <item name="android:paddingBottom">0dp</item>
</style>
```

bottomSheetStyle 与 CustomBottomSheetDialog 是共用的
```xml
<style name="CustomBottomSheet" parent="Widget.MaterialComponents.BottomSheet.Modal">
<!--        指定 BottomSheet 的圆角-->
<item name="shapeAppearance">@style/BottomSheetDialogShapeAppearance</item>
<!--        指定 BottomSheet 的背景色-->
<item name="backgroundTint">?attr/colorSurface</item>
<!--        指定最宽宽度，规避横屏情况下，BottomSheet 宽度显示异常的问题-->
<item name="android:maxWidth">480dp</item>
<!--        指定折叠状态高度，规避横屏情况下，折叠状态高度过低的问题-->
<item name="behavior_peekHeight">240dp</item>
<!--        指定 Expanded 状态下不清除圆角-->
<item name="shouldRemoveExpandedCorners">false</item>
</style>
```