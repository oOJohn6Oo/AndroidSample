package io.john6.sample.cleanarch.presentation.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.john6.sample.cleanarch.domain.model.PostModel
import io.john6.sample.cleanarch.interactor.GetPostsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PostListViewModel(private val getPostsUseCase: GetPostsUseCase) : ViewModel() {

    private val _postList = MutableStateFlow(PostListUiState())
    val postList = _postList.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PostListUiState()
    )

    init {
        onUiEvent(PostListUiEvent.LoadPosts)
    }

    fun onUiEvent(event: PostListUiEvent) {
        when (event) {
            is PostListUiEvent.LoadPosts -> {
                getPostsUseCase.execute().onEach { result ->
                    if (_postList.value.isLoading != result.isLoading) {
                        _postList.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    if (result.errorResId != 0) {
                        _postList.update {
                            it.copy(errorResId = result.errorResId)
                        }
                        return@onEach
                    }
                    if (result.errorMsg.isNotEmpty()) {
                        _postList.update {
                            it.copy(error = result.errorMsg)
                        }
                        return@onEach
                    }
                    if (result.isSuccess()) {
                        _postList.update {
                            it.copy(postList = result.data ?: emptyList())
                        }
                    }
                }.launchIn(viewModelScope)
            }

            PostListUiEvent.ConsumeError -> {
                _postList.update {
                    it.copy(error = "", errorResId = 0)
                }
            }
        }
    }

    companion object{
        fun provideFactory() = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PostListViewModel(GetPostsUseCase()) as T
            }
        }
    }
}

data class PostListUiState(
    val postList: List<PostModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String = "",
    val errorResId: Int = 0,
)

sealed class PostListUiEvent {
    object LoadPosts : PostListUiEvent()
    object ConsumeError : PostListUiEvent()
}