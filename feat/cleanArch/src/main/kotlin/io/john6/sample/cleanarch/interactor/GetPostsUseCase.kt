package io.john6.sample.cleanarch.interactor

import io.john6.sample.cleanarch.domain.data.DataState
import io.john6.sample.cleanarch.domain.model.PostModel
import io.john6.sample.data.network.typiCodeRepo
import kotlinx.coroutines.flow.flow

class GetPostsUseCase {
    fun execute() = flow<DataState<List<PostModel>>> {
        emit(DataState.loading())
        try {
            val postList = typiCodeRepo.getPosts().map { PostModel.mapFromPostDTO(it) }
            emit(DataState.success(postList))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown Error"))
        }
    }
}