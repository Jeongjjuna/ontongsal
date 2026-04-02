package yjh.ontongsal.restapi

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.exception.AppException
import yjh.ontongsal.restapi.exception.ErrorCode
import yjh.ontongsal.restapi.port.ArticleRepository
import yjh.ontongsal.restapi.port.CommentRepository
import yjh.ontongsal.restapi.port.TransactionRunner

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
