package io.john6.sample.cleanarch.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.john6.sample.cleanarch.domain.model.PostModel
import io.john6.sample.cleanarch.interactor.GetPostUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PostDetailViewModel(
    private val postId: String,
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {

    private val previousPost =PostModel(id = postId)

    private val _uiState = MutableStateFlow(PostDetailUiState(post = previousPost))
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PostDetailUiState(post = previousPost)
    )

    init {
        if (previousPost.id.isNotEmpty()) {
            onUiEvent(PostDetailUiEvent.LoadPost)
        }
    }

    fun onUiEvent(event: PostDetailUiEvent) {
        when (event) {
            is PostDetailUiEvent.LoadPost -> {
                getPostUseCase.execute(previousPost.id).onEach { result ->
                    if (_uiState.value.isLoading != result.isLoading) {
                        _uiState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    if (result.errorResId != 0) {
                        _uiState.update {
                            it.copy(errorResId = result.errorResId)
                        }
                        return@onEach
                    }
                    if (result.errorMsg.isNotEmpty()) {
                        _uiState.update {
                            it.copy(error = result.errorMsg)
                        }
                        return@onEach
                    }
                    if (result.isSuccess()) {
                        _uiState.update {
                            it.copy(post = result.data ?: PostModel())
                        }
                    }
                }.launchIn(viewModelScope)
            }

            PostDetailUiEvent.ConsumeError -> {
                _uiState.update {
                    it.copy(error = "", errorResId = 0)
                }
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(postId: String) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PostDetailViewModel(
                    postId = postId,
                    getPostUseCase = GetPostUseCase(),
                ) as T
            }
        }
    }
}

data class PostDetailUiState(
    val post: PostModel = PostModel(),
    val isLoading: Boolean = false,
    val error: String = "",
    val errorResId: Int = 0,
)

sealed class PostDetailUiEvent {
    object LoadPost : PostDetailUiEvent()
    object ConsumeError : PostDetailUiEvent()
}