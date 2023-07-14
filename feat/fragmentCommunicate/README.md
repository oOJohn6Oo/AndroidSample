## Fragment 间通信最佳实践

> 这里以 Activity、Fragment 调用 DialogFragment 为例，演示如何通过 FragmentResult API 实现 Fragment 间通信。

![Home Page]

### 1. Fragment 间通信的方式

<p>
<img src="https://miro.medium.com/v2/resize:fit:2000/format:webp/1*MX8AYVjiroArN6AouD-afg.png" width="600" />
</p>

1. ViewModel
2. ~~TargetFragment~~
3. Interface
4. FragmentResult API

之所以认为 FragmentResult API 最佳，主要有以下几个原因：
1. 它是 Fragment 自身的 API，使用 FragmentManager 作为中介完成通信，不需要依赖其他类库
2. 类似消息总线的方式可以更好地把 Fragment 与 Activity 解耦，实现 Fragment 多处复用
3. 定义相对其他三种来说比较简单，维护成本较低

### 2. 演示说明

> Activity 及 Fragment 可通过调用 [DemoDialogFragment] 显示一个弹窗，滑动弹窗中的 Slider 选中一个值按 OK 按钮后，可将此值回调给调用方。</br>
> 由于多个调用方都是使用的同一个 DialogFragment，所以通过传入的 tag 作为标记，将结果回调给对应调用方。

* [FragmentCommunicateActivity] 演示 Fragment 间通信的 Activity, 也是模块唯一的 Activity，UI 由两个 [DemoFragment] + [CommonScreen] 组成
* [DemoFragment] 仅展示获取到的值信息
* [DemoDialogFragment] 供调用方使用的弹窗
* [DemoViewModel] 用于存储信息，防止 Activity 重建时数据丢失



[FragmentCommunicateActivity]: ./src/main/kotlin/io/john6/sample/fragmentcommunicate/FragmentCommunicateActivity.kt
[DemoFragment]: ./src/main/kotlin/io/john6/sample/fragmentcommunicate/DemoFragment.kt
[DemoDialogFragment]: ./src/main/kotlin/io/john6/sample/fragmentcommunicate/DemoDialogFragment.kt
[DemoViewModel]: ./src/main/kotlin/io/john6/sample/fragmentcommunicate/DemoViewModel.kt
[CommonScreen]: ./src/main/kotlin/io/john6/sample/fragmentcommunicate/CommonScreen.kt
[Home Page]: https://github.com/oOJohn6Oo/AndroidSample/releases/download/v0.1/fc_main_screen.webp