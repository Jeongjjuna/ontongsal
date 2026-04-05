package yjh.ontongsal.restapi.adpater.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import yjh.ontongsal.restapi.domain.CommentContent

data class CommentUpdateRequest(
    @field:NotNull(message = "댓글 내용은 필수입니다.")
    @field:NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    val content: String,
) {
    fun toContent(): CommentContent {
        return CommentContent(content)
    }
}
