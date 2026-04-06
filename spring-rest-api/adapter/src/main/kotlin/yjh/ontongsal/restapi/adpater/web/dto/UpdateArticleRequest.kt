package yjh.ontongsal.restapi.adpater.web.dto

import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.ArticleTitle

data class UpdateArticleRequest(
    val title: String,
    val content: String,
) {

    fun toTitle(): ArticleTitle = ArticleTitle(title)

    fun toContent(): ArticleContent = ArticleContent(content)
}
