package io.john6.sample.cleanarch.presentation.ui.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.john6.base.compose.spaceLarge
import io.john6.sample.cleanarch.domain.model.PostModel
import io.john6.sample.cleanarch.presentation.ui.widget.EmptyView
import io.john6.sample.cleanarch.presentation.ui.widget.LoadingView


@Composable
fun PostDetailScreen(
    postDetailUiState: PostDetailUiState,
    onUiEvent: (PostDetailUiEvent) -> Unit,
    onBack: () -> Unit,
) {
    if (postDetailUiState.post.id.isEmpty()) {
        onBack()
        return
    }
    if (postDetailUiState.error.isNotEmpty()) {
        Toast.makeText(LocalContext.current, postDetailUiState.error, Toast.LENGTH_SHORT).show()
        onUiEvent(PostDetailUiEvent.ConsumeError)
    }
    if (postDetailUiState.errorResId != 0) {
        Toast.makeText(
            LocalContext.current,
            stringResource(postDetailUiState.errorResId),
            Toast.LENGTH_SHORT
        ).show()
        onUiEvent(PostDetailUiEvent.ConsumeError)
    }

    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colors.onBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(MaterialTheme.spaceLarge)
        ) {
            when {
                postDetailUiState.isLoading -> {
                    LoadingView(modifier = Modifier.fillMaxSize().background(Color.Transparent))
                }

                postDetailUiState.post.body.isNotEmpty() -> {
                    PostDetailItem(postDetailUiState.post)
                }

                else -> {
                    EmptyView { onUiEvent(PostDetailUiEvent.LoadPost) }
                }
            }
        }
    }
}

@Composable
fun PostDetailItem(post: PostModel) {
    Text(text = post.title, style = MaterialTheme.typography.h5)
    Text(text = post.userId, style = MaterialTheme.typography.caption)
    Text(text = post.body, style = MaterialTheme.typography.body1)
}

@Preview
@Composable
fun PreviewPostDetailScreen() {
    PostDetailScreen(postDetailUiState = PostDetailUiState(), onUiEvent = {}) {
    }
}