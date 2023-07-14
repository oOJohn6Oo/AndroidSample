## 各种 Dialog 实现最佳实践

<img src="https://github.com/oOJohn6Oo/AndroidSample/releases/download/v0.1/dialog_main_screen.webp" width="480" />


https://github.com/oOJohn6Oo/AndroidSample/assets/24718357/5b2d13a4-0fb0-4a89-9172-f04fff667808

https://github.com/oOJohn6Oo/AndroidSample/assets/24718357/35b369c7-6184-47cf-a59b-1a74f63fcd6f

### 包含的 Dialog 类型

1. [DemoFullScreenDialog] 全屏 Dialog
2. [DemoNormalDialog] 普通 AlertDialog
3. [DemoModalBottomSheetDialog] 使用 BottomSheetDialog 实现的默认底部弹窗
4. [DemoCustomBottomSheetDialog] 定制 BottomSheetDialog 样式实现的底部弹窗

这四种几乎涵盖了 APP 内所有的弹窗样式

### 演示说明

> 所有 Dialog 均包含一个 RecyclerView<br/>
> 滑动 Slider 调节 RecyclerView 中的 Item 数量<br/>
> 所有 Dialog 均完成了横竖屏、黑暗模式适配<br/>




[DemoFullScreenDialog]: ./src/main/kotlin/io/john6/sample/dialog/DemoFullScreenDialog.kt
[DemoNormalDialog]: ./src/main/kotlin/io/john6/sample/dialog/DemoNormalDialog.kt
[DemoModalBottomSheetDialog]: ./src/main/kotlin/io/john6/sample/dialog/DemoModalBottomSheetDialog.kt
[DemoCustomBottomSheetDialog]: ./src/main/kotlin/io/john6/sample/dialog/DemoCustomBottomSheetDialog.kt
[Home Page]: https://github.com/oOJohn6Oo/AndroidSample/releases/download/v0.1/dialog_main_screen.webp

[Modal Video]: https://github.com/oOJohn6Oo/AndroidSample/releases/download/v0.1/modal_bottom_sheet.mp4
[Custom Video]: https://github.com/oOJohn6Oo/AndroidSample/releases/download/v0.1/custom_bottom_sheet.mp4
