package io.john6.sample.loadimage

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.john6.johnbase.util.ProgressHelper
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceLarge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class WriteTest : ComponentActivity() {

    private var tempDrawable: ColorDrawable? = null

    private val requestWriteLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            tempDrawable?.let {
                save2Album(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            WriteScreen(this::randomColor, saveToAlbum = this::save2Album)
        }
    }


    private fun randomColor(): Int {
        return android.graphics.Color.rgb(
            (0..255).random(), (0..255).random(), (0..255).random()
        )
    }

    private fun save2Album(drawable: ColorDrawable) {
        tempDrawable = drawable
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !isPermissionGranted(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            requestWriteLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }
        lifecycleScope.launch {
            ProgressHelper.show(this@WriteTest)
            withContext(Dispatchers.IO) {
                val file = File(externalCacheDir, "IMG_temp.jpg")
                drawable.toBitmap(100, 100, Bitmap.Config.RGB_565).compress(
                    Bitmap.CompressFormat.JPEG, 100, file.outputStream()
                )
                save2Album(
                    contentResolver = contentResolver,
                    srcImgPath = file.absolutePath,
                    saveDirName = "johntest",
                    imageWidth = 100,
                    imageHeight = 100
                )
            }
            ProgressHelper.dismiss(this@WriteTest)
            Toast.makeText(this@WriteTest, android.R.string.ok, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun WriteScreen(randomColor: () -> Int, saveToAlbum: (ColorDrawable) -> Unit) {
    JohnAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(MaterialTheme.spaceLarge)
        ) {
            val onBbg = MaterialTheme.colors.onBackground.toArgb()
            var currentColor by rememberSaveable {
                mutableStateOf(onBbg)
            }

            val drawable = remember(currentColor) {
                ColorDrawable(currentColor)
            }

            val doRandom = remember {
                {
                    val color = randomColor()
                    currentColor = color
                }
            }

            val doSave = remember {
                {
                    saveToAlbum(drawable)
                }
            }
            val isLandScape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .align(Alignment.TopCenter)
                    .aspectRatio(1f)
                    .background(Color(currentColor), RoundedCornerShape(12.dp))
            )
            if (isLandScape) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                ) {
                    BtnContent(doRandom, doSave)
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    BtnContent(doRandom, doSave)
                }
            }

        }
    }
}

@Composable
fun BtnContent(randomColor: () -> Unit, saveToAlbum: () -> Unit) {
    IconButton(onClick = randomColor) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Random Color",
            tint = MaterialTheme.colors.onBackground
        )
    }
    Spacer(modifier = Modifier.width(MaterialTheme.spaceLarge))
    IconButton(
        onClick = saveToAlbum,
    ) {
        Icon(
            imageVector = Icons.Default.SaveAlt,
            contentDescription = "Save to Album",
            tint = MaterialTheme.colors.onBackground
        )
    }
}