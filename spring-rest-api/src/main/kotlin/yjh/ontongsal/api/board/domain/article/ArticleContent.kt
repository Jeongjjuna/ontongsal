package yjh.ontongsal.api.board.domain.article

import yjh.ontongsal.api.board.domain.Content

class ArticleContent(
    content: String,
) : Content(content) {

    override fun checkLength(content: String) {
        require(content.isNotBlank()) { "게시글 내용은 비어있을 수 없습니다." }
        require(content.length <= 5000) { "게시글 내용은 최대 5000자까지 입력 가능합니다." }
    }
}
