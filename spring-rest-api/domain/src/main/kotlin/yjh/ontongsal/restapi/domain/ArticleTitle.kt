package yjh.ontongsal.restapi.domain

class ArticleTitle(
    title: String,
) : Content(title) {

    override fun checkLength(content: String) {
        require(content.isNotBlank()) { "게시글 제목은 비어있을 수 없습니다." }
        require(content.length <= 100) { "게시글 제목은 최대 100자까지 입력 가능합니다." }
    }
}
