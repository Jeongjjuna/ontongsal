package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.ArticleRepository
import yjh.ontongsal.restapi.application.port.TransactionRunner
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.ArticleTitle
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

private val logger = KotlinLogging.logger { }

@Component
class ArticleUpdater(
    private val articleRepository: ArticleRepository,
    private val transaction: TransactionRunner,
) {

    fun update(
        actor: Actor,
        articleId: TargetId,
        title: ArticleTitle,
        content: ArticleContent,
    ): Article {
        return transaction.run {
            val article: Article = articleRepository.findById(articleId)
                ?: throw AppException.NotFound(ErrorCode.ARTICLE_NOT_FOUND, "게시글을 찾을 수 없습니다. [$articleId]")

            article.update(actor, title, content)
            articleRepository.update(article)
        }.also {
            logger.info { "article modified : articleId=$articleId" }
        }
    }
}
