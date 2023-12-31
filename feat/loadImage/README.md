## 图片读写相关的最佳实践

<img src="https://github.com/oOJohn6Oo/AndroidSample/assets/24718357/05256807-5d49-42a7-ba95-4c5c38849a07" width=320/>

| 读 | 写 
| :----: | :----: 
| 展示了使用3种 Intent 的方式及获取`READ_EXTERNAL_STORAGE`权限的方式来获取图片信息并显示到界面 | 展示了如何兼容所有版本将图片文件写入到`/Pictures`文件夹下 
| <img src="https://github.com/oOJohn6Oo/AndroidSample/assets/24718357/52635a13-02d6-4bc3-9697-83a9941a5d60" width=320/> | <img src="https://github.com/oOJohn6Oo/AndroidSample/assets/24718357/89941b4c-5d5a-4ba9-8ef6-25ab58d1b7be" width=320/>


### 1. 权限申请最小化

```xml
<uses-permission
    android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
<uses-permission
android:name="android.permission.WRITE_EXTERNAL_STORAGE"
android:maxSdkVersion="28" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```

### 2. 从机器中读取图片

#### 1. 通过获取 `READ_EXTERNAL_STORAGE` 权限完成
> 优点：可以自定义选择界面，添加各种过滤条件，比如限制数量，限制大小，限制类型等</br>
> 缺点：需要申请权限

```kotlin
contentResolver.query(
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
    null, null, "$orderBy DESC"
)?.use { cursor ->
    onGetCursor(cursor, null)
}
```

#### 2. 通过 `Intent.ACTION_PICK` Intent 完成
> 优点：不需要申请权限｜可以多选图片｜有的系统有定制的选择器界面</br>
> 缺点：无法限制选择数量

Android 12 荣耀70 上测试可以获取到以下 `columnName`：

```text
instance_id
compilation
disc_number
duration
album_artist
description
picasa_id
resolution
latitude
orientation
artist
author
height
is_drm
bucket_display_name
owner_package_name
f_number
volume_name
date_modified
writer
date_expires
composer
_display_name
scene_capture_type
datetaken
mime_type
bitrate
cd_track_number
_id
iso
xmp
year
_data
_size
album
genre
title
width
longitude
is_favorite
is_trashed
exposure_time
group_id
document_id
generation_added
is_download
generation_modified
is_pending
date_added
mini_thumb_magic
capture_framerate
num_tracks
isprivate
original_document_id
bucket_id
relative_path
```

#### 3. 通过 `Intent.ACTION_GET_CONTENT` Intent 完成
> 优点：不需要申请权限｜可以多选图片</br>
> 缺点：默认的选择界面不好看

Android 12 荣耀70 上测试可以获取到以下 `columnName`：
```text
document_id
mime_type
_display_name
last_modified
flags
_size
```

#### 3. 通过 `MediaStore.ACTION_PICK_IMAGES` Intent 完成
> 优点：不需要申请权限｜可以多选图片 | 统一的选择器界面 | 可以设置数量限制</br>
> 缺点：选择器没有用心做 | Android 13及以上才支持，其他版本会使用`Intent.ACTION_OPEN_DOCUMENT`作为兼容方案</br>
> 或者使用谷歌动态下发的兼容库，但需要GMS支持，可以支持到安卓 5.0

Samsung Galaxy S21 Android 13 上测试可以获取到以下 `columnName`：

```text
duration
orientation
height
_display_name
datetaken
mime_type
_data
_size
width
```


### 3. 存储图片到公共目录,具体可以参考[Util#save2Album]方法

> 需要注意的兼容问题是，Android 10 以下的版本，需要申请`WRITE_EXTERNAL_STORAGE`权限
Android 10 及以上版本不需要</br>
> 通过 MediaStore API 存储图片，版本导致的差异主要在于</br>
> * 安卓 10 以下，使用`contentValues.put(MediaStore.Images.Media.DATA, fullFinalPath)`来指定图片存储路径</br>
> * 安卓 10 及以上，使用`contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)`来指定图片存储路径</br>
> * 安卓 10 及以上，新增`MediaStore.Images.Media.IS_PENDING`字段，标识资源是否正在处理


#### 1. 写入通用图片信息

```kotlin
val contentValues = ContentValues()
addCommonValue(contentValues, finalImageFileName, fileCreatedTime, imageWidth, imageHeight)


/**
 * 给 [ContentValues] 添加通用信息
 *
 * @param contentValues 需要添加信息的 [ContentValues]
 * @param desireDisplayName 期望的文件名
 * @param createdTimeInMillIs 创建时间，单位毫秒
 * @param imageWidth 图片宽度 0 则不写入
 * @param imageHeight 图片高度 0 则不写入
 *
 * media provider uses seconds for DATE_MODIFIED and DATE_ADDED, but milliseconds
 * for DATE_TAKEN
 */
private fun addCommonValue(
    contentValues: ContentValues,
    desireDisplayName: String,
    createdTimeInMillIs: Long,
    imageWidth: Int = 0,
    imageHeight: Int = 0,
) {
    // Save the screenshot to the MediaStore
    contentValues.put(MediaStore.Images.ImageColumns.TITLE, desireDisplayName)
    contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, desireDisplayName)
    contentValues.put(MediaStore.Images.ImageColumns.DATE_TAKEN, createdTimeInMillIs)
    contentValues.put(MediaStore.Images.ImageColumns.DATE_ADDED, createdTimeInMillIs / 1000)
    contentValues.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, createdTimeInMillIs / 1000)
    contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/*")
    if (imageWidth != 0) {
        contentValues.put(MediaStore.Images.ImageColumns.WIDTH, imageWidth)
    }
    if (imageHeight != 0) {
        contentValues.put(MediaStore.Images.ImageColumns.HEIGHT, imageHeight)
    }
}
```

#### 2. 根据版本确定最终存储的Path

```kotlin
if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
    val fullFinalPath =
        StringBuilder(Environment.getExternalStorageDirectory().absolutePath)
            .append(File.separator)
            .append(Environment.DIRECTORY_PICTURES).append(File.separator)
            .append(safeDirName).append(File.separator)
            .append(finalImageFileName)
            .toString()
    File(fullFinalPath).parentFile?.also {
        if (!it.exists()) {
            it.mkdirs()
        }
    }
    fullFinalPath.log()
    contentValues.put(MediaStore.Images.Media.DATA, fullFinalPath)
} else {
    contentValues.put(
        MediaStore.Images.Media.RELATIVE_PATH,
        Environment.DIRECTORY_DOWNLOADS + File.separator + safeDirName
    )
    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
}
```

#### 3. 创建最终的 Uri 并写入信息

```kotlin
// write data to the uri
try {
    // 虽然 Android 10 以下我们有权限直接写入文件，但是这种方式写入的图片不会被系统检测到
    // 需要我们使用发广播的方式通知系统刷新
    // 而使用以下方式写入的图片可以自动在图库中更新
    File(srcImgPath).inputStream().use { fis ->
        contentResolver.openOutputStream(uri)?.use {
            fis.copyTo(it)
        }
    }
    savedRes = true
} catch (e: Exception) {
    // 网上很多“系统检测到 xxx APP 已删除图库中的图片”就是这个导致的
    // 但是讲道理如果写入失败是需要清除调这个 uri 的，所以，确实很难抉择
    contentResolver.delete(uri, null, null)
    e.printStackTrace()
    return false
}
```

### 3. Bonus 通过`Intent.ACTION_GET_CONTENT`创建多种类型文件的选择器

```kotlin
class PickImageAndPDF : ActivityResultContract<String, List<Uri>>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_GET_CONTENT)
            .setType("*/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "image/*"))
    }
    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        if (intent == null) return emptyList()
        if (resultCode != Activity.RESULT_OK) return emptyList()
        val dataUri = intent.data
        if (dataUri != null) return listOf(dataUri)

        return intent.clipData?.let {
            val res = mutableListOf<Uri>()
            val count = it.itemCount
            for (i in 0 until count) {
                it.getItemAt(i)?.uri?.let { uri ->
                    res.add(uri)
                }
            }
            res
        }?: emptyList()
    }
}
```

这样可以同时选 PDF 和 图片，支持多选单选，默认单选，选中后能获取到的 columnName 都一致，Android 12 测试有：

```text
document_id
mime_type
_display_name
last_modified
flags
_size
```

[Util]: ./src/main/kotlin/io/john6/sample/loadimage/Util.kt#L235
