package yjh.ontongsal.restapi.web.dto

import yjh.ontongsal.restapi.ArticleContent

data class WriteArticleRequest(
    val content: String
) {

    fun toContent(): ArticleContent {
        return ArticleContent(content)
    }
}
