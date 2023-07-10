package io.john6.sample.loadimage

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageInfo(
    val name:String = "unknown",
    val folderName:String = "",
    val imageSize:String = "unknown",
    val path:String = "",
    val uri:Uri,
    val takenTime:Long = -1,
    val fileSize:Long = -1,
):Parcelable
