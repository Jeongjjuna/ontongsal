package yjh.ontongsal.api.post.presentation.request

import yjh.ontongsal.api.post.domain.PostUpdateCommand

data class PostUpdateRequest(
    val title: String,
    val content: String,
)

fun PostUpdateRequest.toCommand() = PostUpdateCommand(
    title = title,
    content = content
)
