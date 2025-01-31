package yjh.ontongsal.api.post.domain

import yjh.ontongsal.api.common.BaseErrorCode
import yjh.ontongsal.api.common.ValidationConstant
import yjh.ontongsal.api.common.validate

class PostCreateCommand(
    val title: String,
    val content: String,
) {
    init {
        validate(title.isNotBlank()) { BaseErrorCode.NOT_EMPTY_TITLE }
        validate(content.isNotBlank()) { BaseErrorCode.NOT_EMPTY_CONTENT }
        validate(title.length < ValidationConstant.TITLE_MAX_LENGTH) { BaseErrorCode.TITLE_TOO_LONG }
        validate(content.length < ValidationConstant.CONTENT_MAX_LENGTH) { BaseErrorCode.CONTENT_TOO_LONG }
    }
}
