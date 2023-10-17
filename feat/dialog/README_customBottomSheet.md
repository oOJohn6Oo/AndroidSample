## 默认底部弹窗 重要代码说明

> 这里用的是基本的 Dialog 主题实现的 BottomSheetDialog 弹出效果，同时去除了自带的底部边距，改为自己实现给 RecyclerView 添加底部 Padding
> 同时，启用了导航栏遮罩效果

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
<style name="BaseCustomBottomSheetDialog" parent="Theme.AppCompat.Light.Dialog">
    <!--        启用后，Material 相关控件的颜色会根据 Elevation 的值动态变化-->
    <item name="elevationOverlayEnabled">true</item>
    <item name="elevationOverlayColor">?attr/colorOnSurface</item>
    <item name="bottomSheetStyle">@style/CustomBottomSheet</item>
    <!--        定制 BottomSheetDragHandleView 的样式, Material 2 的 Workaround-->
    <item name="bottomSheetDragHandleStyle">
        @style/Widget.MaterialComponents.BottomSheet.DragHandle
    </item>
    <item name="android:windowAnimationStyle">
        @style/Animation.MaterialComponents.BottomSheetDialog
    </item>

    <item name="android:windowBackground">@null</item>
    <item name="android:windowIsFloating">false</item>
    <item name="android:windowNoTitle">true</item>
    <item name="android:statusBarColor">@android:color/transparent</item>
    <item name="android:navigationBarColor">@android:color/transparent</item>
    <item name="android:navigationBarDividerColor" tools:targetApi="o_mr1">
        @android:color/transparent
    </item>
<!--    这里我们统一设置为 false，然后用 InsetsHelper 去适配-->
    <item name="android:enforceStatusBarContrast" tools:targetApi="q">false</item>
    <item name="android:enforceNavigationBarContrast" tools:targetApi="q">false</item>
    <item name="enableEdgeToEdge">true</item>
</style>
```
bottomSheetStyle 与 ModalBottomSheetDialog 是共用的

```xml
<!--    指定 BottomSheetDragHandleView 的属性-->
<style name="Widget.MaterialComponents.BottomSheet.DragHandle" parent="Widget.Material3.BottomSheet.DragHandle">
    <!--        指定背景色-->
    <item name="tint">?attr/colorOnBackground</item>
    <item name="android:alpha">0.3</item>
    <item name="android:paddingBottom">0dp</item>
</style>
```


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

### 3. 底部留出导航栏高度的空间
```kotlin
mInsetsHelper.onInsetsChanged = {
    mBinding.recyclerViewFgDdbs.setBottomPadding(it.safeDrawing().bottom)
}
```