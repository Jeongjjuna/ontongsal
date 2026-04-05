package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.ArticleRepository
import yjh.ontongsal.restapi.application.port.CommentRepository
import yjh.ontongsal.restapi.application.port.TransactionRunner
import yjh.ontongsal.restapi.domain.*
import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

private val logger = KotlinLogging.logger { }

@Component
class CommentCreator(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,

    private val transaction: TransactionRunner,
) {

    fun create(
        actor: Actor,
        targetId: TargetId,
        commentContent: CommentContent,
    ): Comment {
        return transaction.run {
            val article: Article = articleRepository.findById(targetId)
                ?: throw AppException.NotFound(ErrorCode.ARTICLE_NOT_FOUND)
            commentRepository.save(actor, article, commentContent)
        }.also {
            logger.info { "comment created : commentId=${it.id}, articleId=$targetId" }
        }
    }
}
