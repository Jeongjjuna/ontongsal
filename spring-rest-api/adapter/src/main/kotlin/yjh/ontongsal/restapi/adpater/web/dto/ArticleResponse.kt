package yjh.ontongsal.restapi.adpater.web.dto

import yjh.ontongsal.restapi.domain.Article
import java.time.Instant

data class ArticleResponse(
    val id: Long,
    val boardId: Long,
    val writerId: Long,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val createdBy: String,
    val updatedAt: Instant,
    val updatedBy: String,
) {
    companion object {
        fun of(article: Article): ArticleResponse {
            return ArticleResponse(
                id = article.id,
                boardId = article.boardId,
                writerId = article.writerId,
                title = article.title.contentText,
                content = article.content.contentText,
                createdAt = article.auditInfo.createdAt,
                createdBy = article.auditInfo.createdBy,
                updatedAt = article.auditInfo.updatedAt,
                updatedBy = article.auditInfo.updatedBy
            )
        }

        fun of(articles: List<Article>): List<ArticleResponse> {
            return articles.map { of(it) }
        }
    }
}
