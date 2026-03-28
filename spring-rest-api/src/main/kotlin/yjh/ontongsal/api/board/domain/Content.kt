package yjh.ontongsal.api.board.domain

abstract class Content(
    content: String,
) {
    var contentText: String = content
        private set

    init {
        checkLength(content)
    }

    fun update(newContent: String) {
        checkLength(newContent)
        contentText = newContent
    }

    abstract fun checkLength(content: String)
}
