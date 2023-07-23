package io.john6.sample.data.model

/**
 * Data Transfer Object for Post
 */
data class PostDTO(
    val userId: String,
    val id: String,
    val title: String,
    val body: String,
)