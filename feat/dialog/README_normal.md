## 普通弹窗 重要代码说明

### 1. 主题设置

最重要的就这一个属性，如果不设置，内容会被压缩，类似 contentView 宽高从 `match_parent` 强制变为 `wrap_content`

```xml
<item name="android:windowMinWidthMinor">75%</item>
```