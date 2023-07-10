@file:OptIn(ExperimentalFoundationApi::class)

package io.john6.sample.loadimage

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.coroutineScope
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceLarge
import io.john6.johnbase.compose.spaceMedium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.Date

class ReadTest : ComponentActivity() {

    private val vm by viewModels<ReadTestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ReadScreen(
                uiState = vm.uiState.collectAsStateWithLifecycle().value,
                fireUIEvent = vm::onUiEvent,
                isPermissionGranted = this::isPermissionGranted
            )
        }
    }

    private fun isPermissionGranted(): String? {
        val permission =
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.S_V2) {
                Manifest.permission.READ_EXTERNAL_STORAGE
            } else {
                Manifest.permission.READ_MEDIA_IMAGES
            }
        return if (isPermissionGranted(this, permission)) null else permission
    }

}

@Composable
private fun ReadScreen(
    uiState: ReadTestUiState,
    fireUIEvent: (ReadTestUIEvent) -> Unit,
    isPermissionGranted: () -> String?,
) {
    JohnAppTheme {

        val pickActionLauncher = rememberLauncherForActivityResult(PickImage()) {
            if (it.isNotEmpty()) {
                fireUIEvent(ReadTestUIEvent.LoadImage(it))
            }
        }

        val openActionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) {
            if (it.isNotEmpty()) {
                fireUIEvent(ReadTestUIEvent.LoadImage(it))
            }
        }

        val pickVisualMediaLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia()
        ) { result ->
            if (result.isNotEmpty()) {
                fireUIEvent(ReadTestUIEvent.LoadImage(result))
            }
        }


        val ctx = LocalContext.current

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                fireUIEvent(ReadTestUIEvent.LoadImage())
            } else {
                Toast.makeText(ctx, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        if (uiState.doChooseMedia) {
            when (uiState.selectedTypeIndex) {
                0 -> {
                    isPermissionGranted()?.also {
                        requestPermissionLauncher.launch(it)
                    } ?: fireUIEvent(ReadTestUIEvent.LoadImage())
                }

                1 -> pickActionLauncher.launch("image/*")
                2 -> openActionLauncher.launch("image/*")
                3 -> pickVisualMediaLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
            fireUIEvent(ReadTestUIEvent.ChooseMedia(false))
        }

        if (uiState.showChooseTypeDialog) {
            ChooseTypeDialog(selectedTypeIndex = uiState.selectedTypeIndex,
                onTypeChanged = {
                    fireUIEvent(ReadTestUIEvent.SelectType(it))
                    fireUIEvent(ReadTestUIEvent.ShowChooseTypeDialog(false))
                }, typeList = uiState.typeList,
                onDismissRequest = {
                    fireUIEvent(ReadTestUIEvent.ShowChooseTypeDialog(false))
                })
        }

        if (uiState.desiredImageInfoToShowInDialog != null) {
            ImageDetailDialog(
                imageInfo = uiState.desiredImageInfoToShowInDialog,
                onDismissRequest = {
                    fireUIEvent(ReadTestUIEvent.ShowImageDetailDialog(null))
                }
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
        ) {
            val maxSpanSize = remember(maxWidth) {
                (maxWidth / 120.dp).toInt()
            }

            var fabHeight by remember {
                mutableStateOf(0.dp)
            }
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(maxSpanSize),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spaceMedium),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spaceMedium),
                contentPadding = WindowInsets.safeContent.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                    .add(
                        WindowInsets(
                            MaterialTheme.spaceLarge,
                            MaterialTheme.spaceLarge,
                            MaterialTheme.spaceLarge,
                            MaterialTheme.spaceLarge * 2 + fabHeight,
                        )
                    ).asPaddingValues()
            ) {
                items(
                    items = uiState.imageInfoList,
                    key = { it.toString() },
                    span = { GridItemSpan(if (it.uri.scheme != "content") maxSpanSize else 1) }) {

                    ImageItem(it) {
                        if (it.uri.toString().contains("://")) {
                            fireUIEvent(ReadTestUIEvent.ShowImageDetailDialog(it))
                        }
                    }
                }
            }
            val localDensity: Density = LocalDensity.current
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing
                            .add(
                                WindowInsets(
                                    MaterialTheme.spaceLarge,
                                    MaterialTheme.spaceLarge,
                                    MaterialTheme.spaceLarge,
                                    MaterialTheme.spaceLarge,
                                )
                            )
                            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                    )
                    .onSizeChanged {
                        fabHeight = with(localDensity) { it.height.toDp() }
                    },
                shape = CircleShape,
                elevation = 8.dp
            ) {
                Icon(
                    Icons.Default.Preview,
                    contentDescription = null,
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {
                                fireUIEvent(ReadTestUIEvent.ChooseMedia(true))
                            },
                            onLongClick = {
                                fireUIEvent(ReadTestUIEvent.ShowChooseTypeDialog(true))
                            }
                        )
                        .padding(MaterialTheme.spaceLarge)
                )
            }
        }
    }
}

@Composable
private fun ImageItem(imageInfo: ImageInfo, onItemClick: () -> Unit) {
    if (imageInfo.uri.scheme != "content") {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.spaceMedium
                ),
            text = imageInfo.uri.toString(),
            color = MaterialTheme.colors.onBackground
        )
    } else {

        var imageSize by remember {
            mutableStateOf(0)
        }

        val context = LocalContext.current

        val bmp = rememberImageBitmapFromWorker(context, imageInfo.uri, imageSize)

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    1.dp,
                    MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                    MaterialTheme.shapes.medium
                )
                .clickable(true, onClick = onItemClick)
                .onSizeChanged {
                    imageSize = it.width
                },
            contentDescription = "",
            bitmap = bmp ?: ImageBitmap.imageResource(android.R.drawable.arrow_down_float),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun rememberImageBitmapFromWorker(
    context: Context,
    uri: Uri,
    desireSize: Int,
): ImageBitmap? {
    val lifecycleOwner = LocalLifecycleOwner.current
    var rememberedImageBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(uri, desireSize) {
        if (desireSize == 0) return@LaunchedEffect
        // Get the coroutine scope for the current Lifecycle
        val coroutineScope = lifecycleOwner.lifecycle.coroutineScope

        // Run the task in a background thread
        coroutineScope.launch(Dispatchers.IO) {
            val bitmap = loadImage(context, uri, desireSize)

            // Update the state of the composable with the loaded bitmap
            withContext(Dispatchers.Main) {
                rememberedImageBitmap = bitmap?.asImageBitmap()
            }
        }

    }

    return rememberedImageBitmap
}


/**
 * 图片详细信息弹窗
 */
@Composable
private fun ImageDetailDialog(imageInfo: ImageInfo, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest,
        content = {
            Card(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.large,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(MaterialTheme.spaceLarge)
                ) {
                    // list all info in imageInfo
                    Text(color = MaterialTheme.colors.onSurface,
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            ) {
                                append("Name: ")
                            }
                            append(imageInfo.name)
                        })
                    Text(color = MaterialTheme.colors.onSurface,
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            ) {
                                append("Uri: ")
                            }
                            append(imageInfo.uri.toString())
                        })
                    Text(
                        color = MaterialTheme.colors.onSurface,
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            ) {
                                append("File Size: ")
                            }
                            append("${imageInfo.fileSize / 1024} KB")
                        }
                    )
                    Text(
                        color = MaterialTheme.colors.onSurface,
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            ) {
                                append("Image Size: ")
                            }
                            append(imageInfo.imageSize)
                        }
                    )
                    Text(
                        color = MaterialTheme.colors.onSurface,
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            ) {
                                append("Folder Name: ")
                            }
                            append(imageInfo.folderName)
                        }
                    )
                    Text(
                        color = MaterialTheme.colors.onSurface,
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            ) {
                                append("Taken Time: ")
                            }
                            append(
                                DateFormat.getDateTimeInstance().format(Date(imageInfo.takenTime))
                            )
                        }
                    )
                    Text(
                        color = MaterialTheme.colors.onSurface,
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            ) {
                                append("Image Path: ")
                            }
                            append(imageInfo.path)
                        }
                    )

                }
            }
        })
}

/**
 * 选择图片类型弹窗
 */
@Composable
private fun ChooseTypeDialog(
    selectedTypeIndex: Int,
    onTypeChanged: (Int) -> Unit,
    typeList: List<String>,
    onDismissRequest: (() -> Unit) = {},
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Card(
                elevation = 4.dp, backgroundColor = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spaceLarge)
                ) {
                    typeList.forEachIndexed { index, type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onTypeChanged(index)
                                },
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                modifier = Modifier.minimumInteractiveComponentSize(),
                                selected = index == selectedTypeIndex,
                                onClick = null
                            )

                            Text(color = MaterialTheme.colors.onSurface, text = type)
                        }
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun PreviewChooseTypeDialog() {
    ChooseTypeDialog(
        selectedTypeIndex = 0,
        onTypeChanged = {},
        typeList = listOf("ACTION_PICK", "ACTION_OPEN", "PickVisualMedia"),
        onDismissRequest = {}
    )
}

@Preview(heightDp = 600)
@Composable
private fun PreviewReadScreen() {
    ReadScreen(ReadTestUiState(), {}, { null })
}