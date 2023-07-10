package io.john6.sample.loadimage

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.john6.johnbase.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


fun isPermissionGranted(context: Context, permission: String): Boolean {
    return context.packageManager.checkPermission(
        permission, context.packageName
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * 从 [uri] 加载图片，以 [Bitmap] 形式返回
 *
 * @param context 读取 Uri 中的数据需要
 * @param desireSize 调用方想要的大小，通过大小比较后计算出压缩比例，缓解内存压力，注意，如果大于原图尺寸无效
 */
suspend fun loadImage(context: Context, uri: Uri, desireSize:Int): Bitmap? = withContext(Dispatchers.IO){
    val time = System.currentTimeMillis()
    val options = context.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.Options().also { op ->
            op.inJustDecodeBounds = true
            BitmapFactory.decodeStream(it, null, op)
            op.inSampleSize = calculateInSampleSize(op, desireSize, desireSize)
            op.inJustDecodeBounds = false
        }
    }?:return@withContext null

    val bmp = context.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, options)
    }
    "success:${bmp != null} took ${System.currentTimeMillis() - time}ms to loadImage $uri".log()
    return@withContext bmp
}

fun calculateInSampleSize(
    options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
): Int {
    val width = options.outWidth
    val height = options.outHeight
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        while (halfHeight / inSampleSize >= reqHeight
            && halfWidth / inSampleSize >= reqWidth
        ) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

/**
 * 从本地存储中获取图片
 *
 * @param contentResolver [ContentResolver]
 * @param selectedUri 选中的图片的uri,如果为空则读取所有图片(需要权限)
 */
suspend fun getAllImageInfoFromLocalStorage(
    contentResolver: ContentResolver,
    selectedUri: List<Uri>
): List<ImageInfo> = withContext(Dispatchers.IO) {

    // 按文件夹存放图片信息
    val galleryImageUrls = hashMapOf<String, MutableList<ImageInfo>>()

    val orderBy = MediaStore.Images.Media.DATE_TAKEN

    val onGetCursor: (Cursor, Uri?) -> Unit = { cursor, uri ->
        getImageInfoByCursor(cursor, uri) { it:ImageInfo ->
            // 打印出通过 cursor 能获取到的此 uri 的所有信息
            cursor.columnNames.forEach { columnName ->
                columnName.toString().log()
            }
            // 保存到 galleryImageUrls 中
            if (galleryImageUrls.containsKey(it.folderName).not()) {
                galleryImageUrls[it.folderName] = mutableListOf()
            }
            galleryImageUrls[it.folderName]?.add(it)
        }
    }

    // 如果 selectedUri 为空，则读取所有外置存储中的图片信息
    if (selectedUri.isEmpty()) {
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
            null, null, "$orderBy DESC"
        )?.use { cursor ->
            onGetCursor(cursor, null)
        }
    } else { // 否则，读取 selectedUri 中的图片信息
        selectedUri.forEach { uri ->
            contentResolver.query(
                uri, null,
                null, null, "$orderBy DESC"
            )?.use { cursor ->
                onGetCursor(cursor, uri)
            }
        }
    }
    val resList = mutableListOf<ImageInfo>()
    galleryImageUrls.keys.forEach {
        resList.add(ImageInfo(uri = Uri.parse(it)))
        resList.addAll(galleryImageUrls[it] ?: emptyList())
    }
    resList
}

/**
 * 使用 cursor 从系统获取图片信息
 *
 * @param cursor [Cursor]
 * @param uri [Uri]，如果不为空，则使用此 uri 作为最终回调给 [onGetImageInfo]的 [ImageInfo] 的 uri
 * @param onGetImageInfo 获取到图片信息时的回调
 */
fun getImageInfoByCursor(cursor: Cursor,uri: Uri?, onGetImageInfo: (ImageInfo) -> Unit) {
    "getImageInfoByCursor".log()
    val idColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)
    val pathColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
    val folderColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)
    val dateTakenColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
    val widthColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH)
    val heightColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT)
    val displayNameColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
    val sizeColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)

    while (cursor.moveToNext()) {
        try {
            val folderName: String = try {
                cursor.getString(folderColumn) ?: "/"
            } catch (e: Exception) {
                "unknown"
            }
            val path = try {
                cursor.getString(pathColumn) ?: "unknown"
            } catch (e: Exception) {
                "unknown"
            }

            val dateTakenTime = try {
                cursor.getLong(dateTakenColumn)
            } catch (e: Exception) {
                -1L
            }

            val imageSize = try {
                val w = cursor.getLong(widthColumn)
                val h = cursor.getLong(heightColumn)

                if (w <= 0 || h <= 0) "unknown" else "$w x $h"
            } catch (e: Exception) {
                "unknown"
            }

            val name = try {
                cursor.getString(displayNameColumn)
            } catch (e: Exception) {
                "unknown"
            }

            val fileSize = try {
                cursor.getLong(sizeColumn)
            } catch (e: Exception) {
                -1L
            }

            val selectedUri = try {
                ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(idColumn)
                )
            } catch (e: Exception) {
                uri
            } ?: continue

            onGetImageInfo(ImageInfo(name, folderName, imageSize, path, selectedUri, dateTakenTime, fileSize))
        } catch (e: Exception) {
            e.message?.log()
        }
    }
}

/**
 * 保存图片到相册
 *
 * @param context 上下文
 * @param srcImgPath 原始图片路径
 * @param prefix 图片名前缀
 * @param saveDirName 保存目录名
 */
fun save2Album(
    contentResolver: ContentResolver,
    srcImgPath: String,
    prefix: String = "IMG",
    saveDirName: String = "mydir",
    imageWidth: Int = 0,
    imageHeight: Int = 0,
): Boolean {
    var savedRes = false
    val fileCreatedTime = System.currentTimeMillis()
    try {
        val safeDirName = saveDirName.ifBlank { Thread.currentThread().stackTrace[2].className }

        // get final suffix
        val imageTypeString = try {
            srcImgPath.subSequence(srcImgPath.lastIndexOf("."), srcImgPath.length)
        } catch (e: Exception) {
            ".JPG"
        }
        // get final file name
        val formattedTimeString = "${DateFormat.format("yyyyMMddHHmmss", fileCreatedTime)}${
            fileCreatedTime.toString().takeLast(3)
        }"
        val finalImageFileName = "${prefix}_${formattedTimeString}${imageTypeString}"

        val contentValues = ContentValues()
        addCommonValue(contentValues, finalImageFileName, fileCreatedTime, imageWidth, imageHeight)

        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

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

        val uri = contentResolver.insert(contentUri, contentValues) ?: run {
            return false
        }
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

        // update IS_PENDING in the database
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)
        }

        // update file size in the database
        contentValues.clear()
        contentValues.put(MediaStore.Images.ImageColumns.SIZE, File(srcImgPath).length())
        contentResolver.update(uri, contentValues, null, null)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return savedRes
}

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