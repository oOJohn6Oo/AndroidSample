package io.john6.sample.cleanarch.interactor

import io.john6.sample.cleanarch.domain.data.DataState
import io.john6.sample.cleanarch.domain.model.PostModel
import io.john6.sample.data.network.typiCodeRepo
import kotlinx.coroutines.flow.flow

class GetPostUseCase {
    fun execute(postId: String) = flow<DataState<PostModel>> {
        emit(DataState.loading())
        try {
            val post = PostModel.mapFromPostDTO(typiCodeRepo.getPost(postId))
            emit(DataState.success(post))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown Error"))
        }
    }
}