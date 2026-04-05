package yjh.ontongsal.restapi.adpater.web.dto

import yjh.ontongsal.restapi.domain.ArticleContent

data class WriteArticleRequest(
    val content: String
) {

    fun toContent(): ArticleContent {
        return ArticleContent(content)
    }
}
