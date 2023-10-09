@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceLarge
import io.john6.johnbase.compose.spaceMedium

@Composable
fun DemoDoubleStickyHeaderScreen() {
    JohnAppTheme {

        val state = rememberLazyListState()

        // 用于记录第一个 sticky 的高度
        var firstStickySize by remember {
            mutableStateOf(0)
        }

        // 当 fakeStickyHeader item 不在可见 items 中时，用此值判断是否显示 fakeStickyHeader
        val shouldStickyIfNone by remember {
            derivedStateOf {
                state.firstVisibleItemIndex > 2
            }
        }

        // 用于记录是否需要显示第一个 sticky 的背景
        val shouldShowFirstStickyHeaderBackground by remember {
            derivedStateOf {
                state.firstVisibleItemIndex >= 1
            }
        }

        val density = LocalDensity.current
        // 用于记录是否需要显示 fake sticky
        val shouldShowFakeSticky:Boolean by remember {
            derivedStateOf {
                state.layoutInfo.run {
                    if ((visibleItemsInfo.getOrNull(2)?.index ?: 0) >= 4) {
                        return@run true
                    }
                    visibleItemsInfo.find { it.index == 3 }?.let {
                        val beforePadding = beforeContentPadding
                        it.offset + beforePadding <= firstStickySize
                    } ?: shouldStickyIfNone
                }
            }
        }
        val paddingHorizontalForFakeHeader by animateDpAsState(
            targetValue = if (shouldShowFakeSticky) 0.dp else MaterialTheme.spaceLarge,
            label = "animatePaddingHorizontalAsState"
        )
        val state2 = rememberScrollState()

        var sizeFake by remember {
            mutableStateOf(0.dp)
        }
        val fakePinnedItem = movableContentOf { paddingTop: Dp ->
            DemoScrollableRow(
                Modifier
                    .padding(top = paddingTop)
                    .onSizeChanged {
                        val tempSize = with(density) { it.height.toDp() }
                        if (sizeFake != tempSize) {
                            sizeFake = tempSize
                        }
                    }
                    .padding(horizontal = paddingHorizontalForFakeHeader),
                state = state2
            )
        }
        Box(
            modifier = Modifier
                .background(
                    LocalElevationOverlay.current?.apply(
                        color = MaterialTheme.colors.background,
                        elevation = 8.dp
                    ) ?: MaterialTheme.colors.background
                )
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
        ) {
            LazyColumn(
                state = state,
                modifier = Modifier.fillMaxWidth(),
            ) {
                item(key = "firstScrollItem") {
                    DemoNormalItem()
                }
                stickyHeader(key = "firstStickyHeader") {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary.copy(alpha = 0.5f))
                            .fillMaxWidth()
                            .onSizeChanged {
                                firstStickySize = it.height
                            }
                            .padding(MaterialTheme.spaceLarge)
                    ) {
                        Text(text = "firstHeader", color = MaterialTheme.colors.onSurface)
                    }
//                    Column(modifier = Modifier.fillMaxWidth()) {
//                        Box(
//                            modifier = Modifier
//                                .background(if (shouldShowFirstStickyHeaderBackground) MaterialTheme.colors.primary else Color.Transparent)
//                                .fillMaxWidth()
//                                .onSizeChanged {
//                                    firstStickySize = it.height
//                                }
//                                .padding(MaterialTheme.spaceLarge)
//                        ) {
//                            Text(text = "firstStickyHeader", color = MaterialTheme.colors.onSurface)
//                        }
//                        if (shouldShowFakeSticky) {
//                            DemoScrollableRow(
//                                Modifier.padding(horizontal = paddingHorizontalForFakeHeader2),
//                                state = state2
//                            )
//                        }
//                    }
                }
                item(key = "secondScrollItem") {
                    DemoNormalItem()
                }
                stickyHeader {
                    DemoNormalItem("secondHeader", MaterialTheme.colors.primary)
//                    fakePinnedItem(0.dp)
                }
//                item(key = "fakeStickyHeader") {
//                    if (shouldShowFakeSticky) {
//                        Box(modifier = Modifier
//                            .fillMaxWidth()
//                            .height(sizeFake))
//                    }else{
//                        fakePinnedItem(0.dp)
//                    }
//                }
                items(57) {
                    DemoNormalItem()
                }
            }
//            if (shouldShowFakeSticky) {
//                fakePinnedItem(with(density){firstStickySize.toDp()})
//            }
        }
    }
}

@Composable
fun DemoScrollableRow(modifier: Modifier, state:ScrollState) {
    val allList = remember {
        (0..10).map { it.toString() }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .horizontalScroll(state)
            .padding(horizontal = MaterialTheme.spaceLarge, vertical = MaterialTheme.spaceMedium)
    ) {
        allList.forEach {
            Chip(onClick = { }) {
                Text(text = it)
            }
        }
    }
}

@Composable
fun DemoNormalItem(title: String = "Normal Item", background: Color = Color.Unspecified) {
    Box(
        modifier = Modifier
            .background(color = background)
            .padding(horizontal = MaterialTheme.spaceLarge)
            .padding(vertical = MaterialTheme.spaceLarge)
            .fillMaxWidth()
    ) {
//        Text(text = title, modifier = Modifier.padding(120.dp))
        Divider()
        Text(text = title, modifier = Modifier.padding(MaterialTheme.spaceLarge))
    }
}

@Preview
@Composable
fun PreviewDemoDoubleStickyHeaderScreen() {
    DemoDoubleStickyHeaderScreen()
}