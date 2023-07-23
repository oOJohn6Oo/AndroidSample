@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package io.john6.sample.cleanarch.presentation.ui.list

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceLarge
import io.john6.sample.cleanarch.domain.model.PostModel
import io.john6.sample.cleanarch.presentation.ui.list.PostListUiEvent
import io.john6.sample.cleanarch.presentation.ui.list.PostListUiState
import io.john6.sample.cleanarch.presentation.ui.widget.EmptyView
import io.john6.sample.cleanarch.presentation.ui.widget.LoadingDialog


@Composable
fun PostListScreen(postListUiState: PostListUiState, onUiEvent: (PostListUiEvent) -> Unit,
                   goDetailScreen: (PostModel) -> Unit) {
    if (postListUiState.error.isNotEmpty()){
        Toast.makeText(LocalContext.current, postListUiState.error, Toast.LENGTH_SHORT).show()
        onUiEvent(PostListUiEvent.ConsumeError)
    }
    if (postListUiState.errorResId != 0){
        Toast.makeText(
            LocalContext.current,
            stringResource(postListUiState.errorResId),
            Toast.LENGTH_SHORT
        ).show()
        onUiEvent(PostListUiEvent.ConsumeError)
    }
    if (postListUiState.isLoading){
        LoadingDialog()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = WindowInsets.safeDrawing.add(
                WindowInsets(
                    left = MaterialTheme.spaceLarge,
                    top = 0.dp,
                    right = MaterialTheme.spaceLarge,
                    bottom = 0.dp
                )
            ).asPaddingValues()
        ) {
            items(postListUiState.postList.size) { index ->
                PostItem(postListUiState.postList[index], onClick = {
                    goDetailScreen(postListUiState.postList[index])
                })
            }
        }
        if (!postListUiState.isLoading && postListUiState.postList.isEmpty()) {
            EmptyView {
                onUiEvent(PostListUiEvent.LoadPosts)
            }
        }
    }
}


@Composable
fun PostItem(post: PostModel, onClick: () -> Unit = {}) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spaceLarge)) {
        Column(modifier = Modifier.padding(MaterialTheme.spaceLarge)) {
            Text(text = post.title)
            Text(text = post.userId, modifier = Modifier.align(Alignment.End))
        }
    }
}

@Preview
@Composable
fun PreviewPostListScreen() {
    PostListScreen(postListUiState = PostListUiState(), onUiEvent = {}, goDetailScreen = {})
}