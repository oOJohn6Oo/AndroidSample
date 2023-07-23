package io.john6.sample.cleanarch.presentation.ui.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceLarge
import io.john6.sample.cleanarch.domain.model.PostModel
import io.john6.sample.cleanarch.presentation.ui.widget.EmptyView
import io.john6.sample.cleanarch.presentation.ui.widget.LoadingDialog
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(MaterialTheme.spaceLarge)
    ) {
        when {
            postDetailUiState.isLoading -> {
                LoadingView(
                    modifier = Modifier
                        .fillMaxHeight()
                )
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

@Composable
fun PostDetailItem(post: PostModel) {
    Text(text = post.title, style = MaterialTheme.typography.h5)
    Text(text = post.userId, style = MaterialTheme.typography.caption)
    Text(text = post.body, style = MaterialTheme.typography.body1)
}


@Composable
fun PostDetail(post: PostModel) {
    Card {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = post.title)
            Text(text = post.userId, modifier = Modifier.align(Alignment.End))
        }
    }
}