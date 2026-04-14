package yjh.ontongsal.restapi.domain

class BoardName(
    name: String,
) : Content(name) {

    override fun checkLength(content: String) {
        require(content.isNotBlank()) { "게시판 이름은 비어있을 수 없습니다." }
        require(content.length <= 50) { "게시판 이름은 최대 50자까지 입력 가능합니다." }
    }
}
