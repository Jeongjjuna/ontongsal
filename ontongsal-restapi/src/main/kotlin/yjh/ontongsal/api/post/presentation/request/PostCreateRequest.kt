package yjh.ontongsal.api.post.presentation.request

import yjh.ontongsal.api.post.domain.PostCreateCommand

data class PostCreateRequest(
    val title: String,
    val content: String,
)

fun PostCreateRequest.toCommand() = PostCreateCommand(
    title = title,
    content = content
)
