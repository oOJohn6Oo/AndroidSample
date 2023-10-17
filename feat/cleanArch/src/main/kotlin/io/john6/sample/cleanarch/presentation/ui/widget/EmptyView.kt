package io.john6.sample.cleanarch.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.john6.base.compose.spaceLarge

@Composable
fun EmptyView(onBtnRetryClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Info,
            contentDescription = "Warning",
            modifier = Modifier.size(64.dp),
            tint = Color.Red
        )
        Spacer(modifier = Modifier.size(MaterialTheme.spaceLarge))
        Text(text = "No Data")
        Button(onClick = onBtnRetryClick) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        LoadingView(
            Modifier.background(
                MaterialTheme.colors.surface, MaterialTheme.shapes.large
            )
        )
    }
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.padding(MaterialTheme.spaceLarge))
    }
}

@Preview
@Composable
fun PreviewLoadingView() {
    LoadingView(Modifier.fillMaxSize())
}