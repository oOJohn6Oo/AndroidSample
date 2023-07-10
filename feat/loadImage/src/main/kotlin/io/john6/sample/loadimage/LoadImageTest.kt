package io.john6.sample.loadimage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import io.john6.johnbase.compose.JohnAppTheme

class LoadImageTest : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MainScreen(this::startReadTest, this::startWriteTest)
        }
    }

    private fun startReadTest() {
        startActivity(Intent(this, ReadTest::class.java))
    }

    private fun startWriteTest() {
        startActivity(Intent(this, WriteTest::class.java))
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen()
}

@Composable
fun MainScreen(startReadTest: () -> Unit = {}, startWriteTest: () -> Unit = {}) {
    JohnAppTheme {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            val shouldBeHorizontal = maxWidth > maxHeight

            val edgePadding = 24.dp
            val centerPadding = 12.dp

            if (shouldBeHorizontal) {
                val maxW = (maxWidth - edgePadding * 2 - centerPadding) / 2f
                val maxH = maxHeight - edgePadding * 2
                val finalSize = minOf(maxH, maxW)
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainContent(
                        modifier = Modifier.weight(1f),
                        finalSize = finalSize,
                        centerPadding = centerPadding,
                        startReadTest = startReadTest,
                        startWriteTest = startWriteTest
                    )
                }
            } else {
                val maxW = maxWidth - edgePadding * 2
                val maxH = (maxHeight - edgePadding * 2 - centerPadding) / 2f
                val finalSize = minOf(maxH, maxW)
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainContent(
                        modifier = Modifier.weight(1f),
                        finalSize = finalSize,
                        centerPadding = centerPadding,
                        startReadTest = startReadTest,
                        startWriteTest = startWriteTest
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier,
    finalSize: Dp = 0.dp,
    centerPadding: Dp = 12.dp,
    startReadTest: () -> Unit = {}, startWriteTest: () -> Unit = {}
) {
    Spacer(modifier = modifier)
    Button(
        modifier = Modifier.size(finalSize),
        shape = RoundedCornerShape(16.dp),
        onClick = startReadTest
    ) {
        Text(text = "Read Test")
    }
    Spacer(modifier = Modifier.size(centerPadding))
    Button(
        modifier = Modifier.size(finalSize),
        shape = RoundedCornerShape(16.dp),
        onClick = startWriteTest
    ) {
        Text(text = "Write Test")
    }
    Spacer(modifier = modifier)
}
