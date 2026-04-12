package yjh.ontongsal.restapi.adapter.web.dto

import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.ArticleTitle

data class WriteArticleRequest(
    val boardId: Long,
    val title: String,
    val content: String,
) {

    fun toTitle(): ArticleTitle = ArticleTitle(title)

    fun toContent(): ArticleContent = ArticleContent(content)
}
