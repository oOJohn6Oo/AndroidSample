package io.john6.sample.cleanarch.presentation.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.john6.johnbase.compose.spaceLarge

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
        LoadingView()
    }
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Box(modifier = Modifier.padding(MaterialTheme.spaceLarge)) {
            CircularProgressIndicator()
        }
    }
}