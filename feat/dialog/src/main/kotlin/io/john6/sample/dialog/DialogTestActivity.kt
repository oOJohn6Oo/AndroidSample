package io.john6.sample.dialog

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceLarge
import io.john6.sample.data.ReadMeUrlList
import io.john6.sample.ui.WebActivity
import kotlin.math.roundToInt

class DialogTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DialogTestScreen(
                showFullScreenDialog = this::showFullScreenDialog,
                showNormalScreenDialog = this::showNormalScreenDialog,
                showBottomSheetScreenDialog = this::showBottomSheetScreenDialog,
                showCustomBottomSheetScreenDialog = this::showCustomBottomSheetScreenDialog,
                showReadMeScreen = this::showReadMeScreen
            )
        }
    }

    private fun showReadMeScreen(url: String) {
        WebActivity.show(this, url)
    }

    private fun showFullScreenDialog(itemCount: Int) {
        DemoFullScreenDialog.show(supportFragmentManager, itemCount)
    }

    private fun showNormalScreenDialog(itemCount: Int) {
        DemoNormalDialog.show(supportFragmentManager, itemCount)
    }

    private fun showBottomSheetScreenDialog(itemCount: Int) {
        DemoModalBottomSheetDialog.show(supportFragmentManager, itemCount)
    }

    private fun showCustomBottomSheetScreenDialog(itemCount: Int) {
        DemoCustomBottomSheetDialog.show(supportFragmentManager, itemCount)
    }
}

@Composable
private fun DialogTestScreen(
    showFullScreenDialog: (Int) -> Unit,
    showNormalScreenDialog: (Int) -> Unit,
    showBottomSheetScreenDialog: (Int) -> Unit,
    showCustomBottomSheetScreenDialog: (Int) -> Unit,
    showReadMeScreen: (String) -> Unit
) {
    JohnAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
                .padding(horizontal = MaterialTheme.spaceLarge)
                .padding(bottom = MaterialTheme.spaceLarge)
        ) {
            DialogItem("Full Screen",
                { showReadMeScreen(ReadMeUrlList.Dialog.fullscreenDialogUrl) },showFullScreenDialog)
            DialogItem("Normal Prompt",{ showReadMeScreen(ReadMeUrlList.Dialog.normalDialogUrl) },showNormalScreenDialog)
            DialogItem("ModalBottomSheet",{ showReadMeScreen(ReadMeUrlList.Dialog.modalDialogUrl) },showBottomSheetScreenDialog)
            DialogItem("CustomBottomSheet",{ showReadMeScreen(ReadMeUrlList.Dialog.customDialogUrl) },showCustomBottomSheetScreenDialog)
        }
    }
}

@Composable
private fun DialogItem(
    title:String,
    onReadMeBtnClicked:()->Unit,
    onShowBtnClicked:(Int)->Unit) {
    Card(modifier = Modifier.padding(top = MaterialTheme.spaceLarge)) {
        Column(modifier = Modifier.padding(MaterialTheme.spaceLarge)) {
            var itemSize by rememberSaveable {
                mutableStateOf(defaultItemCount.toFloat())
            }

            Text(text = title, style = MaterialTheme.typography.h6)

            if (title.isNotBlank()){
                Slider(
                    value = itemSize,
                    valueRange = 1f..100f,
                    onValueChange = { itemSize = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                val text = buildAnnotatedString {
                    appendInlineContent("icon_list", "[icon]")
                    append(itemSize.roundToInt().toString())
                }
                Text(
                    text = text,
                    inlineContent = mapOf(
                        Pair(
                            "icon_list",
                            InlineTextContent(
                                Placeholder(
                                    width = LocalTextStyle.current.fontSize,
                                    height = LocalTextStyle.current.fontSize,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                                )
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    imageVector = Icons.Default.List,
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.onBackground
                                )
                            }
                        )
                    ),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onReadMeBtnClicked) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "Icon Info")
                }
                IconButton(onClick = { onShowBtnClicked(itemSize.roundToInt()) }) {
                    Icon(imageVector = Icons.Default.Preview, contentDescription = "Icon Preview")
                }
            }
        }
    }
}

@Preview(heightDp = 400)
@Composable
fun PreviewDialogTestScreen() {
    DialogTestScreen(
        showFullScreenDialog = {},
        showNormalScreenDialog = {},
        showBottomSheetScreenDialog = {},
        showCustomBottomSheetScreenDialog = {},
        showReadMeScreen = {}
    )
}