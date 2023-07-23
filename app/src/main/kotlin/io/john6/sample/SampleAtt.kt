@file:OptIn(ExperimentalMaterialApi::class)
@file:Suppress("SameParameterValue")

package io.john6.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceLarge
import io.john6.johnbase.compose.spaceMedium
import io.john6.sample.data.ReadMeUrlList
import io.john6.sample.ui.WebActivity

class SampleAtt : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SampleScreen(this::onClickItem, this::onShowModuleReadMe, this::onClickDetailIcon)
        }
    }

    private fun onShowModuleReadMe(moduleName: String) {
        WebActivity.show(this, "${ReadMeUrlList.host}/feat/$moduleName/README.md")
    }

    private fun onClickItem(moduleInfo: FeatureModuleInfo) {
        startActivity(Intent(this, moduleInfo.clazz))
    }

    private fun onClickDetailIcon() {
        WebActivity.show(this, ReadMeUrlList.projectReadMeUrl)
    }
}

@Composable
private fun SampleScreen(
    onClickItem: (FeatureModuleInfo) -> Unit,
    onShowModuleReadMe: (String) -> Unit,
    onClickDetailIcon: () -> Unit
) {
    JohnAppTheme {

        val state = rememberLazyListState()
        val topAppBarElevation by animateDpAsState(
            if (state.canScrollBackward) 8.dp else 0.dp, label = "topAppBarElevation"
        )

        Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
            Surface(
                elevation = topAppBarElevation,
                modifier = Modifier.zIndex(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                            )
                        )
                        .padding(vertical = MaterialTheme.spaceLarge),
                    contentAlignment = Alignment.Center
                ) {
                    // 标题
                    Text(
                        text = "Sample",
                        style = MaterialTheme.typography.h4
                    )

                    // ”Info“ 按钮
                    IconButton(
                        onClick = onClickDetailIcon,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = MaterialTheme.spaceLarge)
                    ) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = "Info")
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                    .asPaddingValues(),
                state = state,
            ) {
                items(items = allAvailableModuleInfo, key = { it.title }) {
                    FeatureItem(it, showModuleReadMe = {
                        onShowModuleReadMe(it.moduleName)
                    }) {
                        onClickItem(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(featureModuleInfo: FeatureModuleInfo,
                        showModuleReadMe:()->Unit,
                        onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spaceLarge, vertical = MaterialTheme.spaceMedium),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spaceLarge)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = featureModuleInfo.title,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h6
                )
                IconButton(onClick = showModuleReadMe,) {
                    Icon(imageVector = Icons.Filled.Info, contentDescription = "Info", tint = MaterialTheme.colors.primary)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spaceLarge))
            Text(
                text = featureModuleInfo.desc,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Preview(heightDp = 600)
@Composable
private fun PreviewSampleScreen() {
    SampleScreen({}, {},{})
}