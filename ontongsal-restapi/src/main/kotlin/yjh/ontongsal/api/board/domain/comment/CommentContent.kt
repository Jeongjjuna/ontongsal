package yjh.ontongsal.api.board.domain.comment

import yjh.ontongsal.api.board.domain.Content

class CommentContent(
    content: String
): Content(content) {

    override fun checkLength(content: String) {
        require(content.isNotBlank()) { "댓글 내용은 비어있을 수 없습니다." }
        require(content.length <= 5000) { "댓글 내용은 최대 5000자까지 입력 가능합니다." }    }
}
