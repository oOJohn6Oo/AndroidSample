package io.john6.sample.cleanarch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.sample.cleanarch.presentation.ui.detail.PostDetailScreen
import io.john6.sample.cleanarch.presentation.ui.detail.PostDetailViewModel
import io.john6.sample.cleanarch.presentation.ui.list.PostListScreen
import io.john6.sample.cleanarch.presentation.ui.list.PostListViewModel
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            JohnAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = SampleNavDestination.postList
                ) {
                    composable(SampleNavDestination.postList) {
                        val postListViewModel = viewModel(
                            factory = PostListViewModel.provideFactory(),
                            modelClass = PostListViewModel::class.java
                        )
                        PostListScreen(
                            postListUiState = postListViewModel.postList.collectAsStateWithLifecycle().value,
                            onUiEvent = postListViewModel::onUiEvent,
                            goDetailScreen = {
                                navController.navigate(SampleNavDestination.postDetail + File.pathSeparator + it.id)
                            }
                        )
                    }
                    composable(SampleNavDestination.postDetail + File.pathSeparator + "{id}",
                        arguments = listOf(
                            navArgument("id") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val postDetailViewModel = viewModel(
                            factory = PostDetailViewModel.provideFactory(
                                it.arguments?.getString("id") ?: ""
                            ),
                            modelClass = PostDetailViewModel::class.java
                        )
                        PostDetailScreen(
                            postDetailUiState = postDetailViewModel.uiState.collectAsStateWithLifecycle().value,
                            onUiEvent = postDetailViewModel::onUiEvent,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}