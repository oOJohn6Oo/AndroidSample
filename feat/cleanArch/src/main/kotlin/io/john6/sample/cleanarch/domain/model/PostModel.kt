package io.john6.sample.cleanarch.domain.model

import android.os.Parcelable
import io.john6.sample.data.model.PostDTO
import kotlinx.parcelize.Parcelize

/**
 * model for Post
 */
@Parcelize
data class PostModel(
    val userId: String = "",
    val id: String = "",
    val title: String = "",
    val body: String = "",
) : Parcelable {
    companion object{
        const val KEY = "postModel"
        fun mapFromPostDTO(postDTO: PostDTO): PostModel {
            return PostModel(
                userId = postDTO.userId,
                id = postDTO.id,
                title = postDTO.title,
                body = postDTO.body
            )
        }
    }
}