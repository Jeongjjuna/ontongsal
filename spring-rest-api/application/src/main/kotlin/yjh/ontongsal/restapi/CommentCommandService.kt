package yjh.ontongsal.restapi

import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.dto.CommentDeleteCommand
import yjh.ontongsal.restapi.dto.CommentUpdateCommand
import yjh.ontongsal.restapi.dto.CommentWriteCommand
import yjh.ontongsal.restapi.exception.AppException
import yjh.ontongsal.restapi.exception.ErrorCode
import yjh.ontongsal.restapi.port.DeleteCommentPort
import yjh.ontongsal.restapi.port.EventPublisher
import yjh.ontongsal.restapi.port.LoadArticlePort
import yjh.ontongsal.restapi.port.SaveCommentPort
import yjh.ontongsal.restapi.port.TransactionRunner
import yjh.ontongsal.restapi.usecase.DeleteCommentUseCase
import yjh.ontongsal.restapi.usecase.UpdateCommentUseCase
import yjh.ontongsal.restapi.usecase.WriteCommentUseCase

@Service
class CommentCommandService(
    private val loadArticlePort: LoadArticlePort,
    private val saveCommentPort: SaveCommentPort,
    private val deleteCommentPort: DeleteCommentPort,
    private val tx: TransactionRunner,
    private val eventPublisher: EventPublisher,
) : WriteCommentUseCase, UpdateCommentUseCase, DeleteCommentUseCase {

    override fun write(command: CommentWriteCommand): Long {

        val comment = tx.run {
            val article: Article = loadArticlePort.findById(command.articleId)
                ?: throw AppException.NotFound(ErrorCode.ARTICLE_NOT_FOUND, "Article ${command.articleId} not found")

            val actor = Actor(command.actorId, command.actorName)
            val comment = Comment.create(article, actor, command.content)

            saveCommentPort.create(comment)
        }

        val event = CommentCreatedEvent(articleId = command.articleId, commentId = comment.id)
        eventPublisher.publish(event)

        return comment.id
    }

    override fun update(command: CommentUpdateCommand): Long {
        TODO("Not yet implemented")
    }

    override fun delete(commentDeleteCommand: CommentDeleteCommand) {
        TODO("Not yet implemented")
    }
}
