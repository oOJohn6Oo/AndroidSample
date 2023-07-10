## 各种 Dialog 实现最佳实践

![Home Page]

### 1. 包含的 Dialog 类型

1. [DemoFullScreenDialog] 全屏 Dialog
2. [DemoNormalDialog] 普通 AlertDialog
3. [DemoModalBottomSheetDialog] 使用 BottomSheetDialog 实现的默认底部弹窗
4. [DemoCustomBottomSheetDialog] 定制 BottomSheetDialog 样式实现的底部弹窗

这四种几乎涵盖了 APP 内所有的弹窗样式

### 2. 演示说明

> 所有 Dialog 均包含一个 RecyclerView
> 滑动 Slider 调节 RecyclerView 中的 Item 数量
> 所有 Dialog 均完成了横竖屏、黑暗模式适配




[DemoFullScreenDialog]: ./src/main/kotlin/io/john6/sample/dialog/DemoFullScreenDialog.kt
[DemoNormalDialog]: ./src/main/kotlin/io/john6/sample/dialog/DemoNormalDialog.kt
[DemoModalBottomSheetDialog]: ./src/main/kotlin/io/john6/sample/dialog/DemoModalBottomSheetDialog.kt
[DemoCustomBottomSheetDialog]: ./src/main/kotlin/io/john6/sample/dialog/DemoCustomBottomSheetDialog.kt
[Home Page]: ./feat/HomePage/img.webp