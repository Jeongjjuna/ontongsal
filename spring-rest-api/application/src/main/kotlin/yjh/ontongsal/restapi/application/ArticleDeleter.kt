package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.ArticleRepository
import yjh.ontongsal.restapi.application.port.TransactionRunner
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

private val logger = KotlinLogging.logger { }

@Component
class ArticleDeleter(
    private val articleRepository: ArticleRepository,
    private val transaction: TransactionRunner,
) {

    fun delete(actor: Actor, articleId: TargetId) {
        transaction.run {
            val article: Article = articleRepository.findById(articleId)
                ?: throw AppException.NotFound(ErrorCode.ARTICLE_NOT_FOUND, "게시글을 찾을 수 없습니다. [$articleId]")
            article.delete(actor)
            articleRepository.delete(article)
        }.also {
            logger.info { "article deleted : articleId=$articleId" }
        }
    }
}
