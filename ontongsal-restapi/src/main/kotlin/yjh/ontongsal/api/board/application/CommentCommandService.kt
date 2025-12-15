package yjh.ontongsal.api.board.application

import org.springframework.stereotype.Service
import yjh.ontongsal.api.board.application.input.DeleteCommentUseCase
import yjh.ontongsal.api.board.application.input.UpdateCommentUseCase
import yjh.ontongsal.api.board.application.input.WriteCommentUseCase
import yjh.ontongsal.api.board.application.input.dto.CommentDeleteCommand
import yjh.ontongsal.api.board.application.input.dto.CommentUpdateCommand
import yjh.ontongsal.api.board.application.input.dto.CommentWriteCommand
import yjh.ontongsal.api.board.application.output.DeleteCommentPort
import yjh.ontongsal.api.board.application.output.EventPublisher
import yjh.ontongsal.api.board.application.output.LoadArticlePort
import yjh.ontongsal.api.board.application.output.SaveCommentPort
import yjh.ontongsal.api.board.domain.CommentCreatedEvent
import yjh.ontongsal.api.board.domain.article.Article
import yjh.ontongsal.api.board.domain.comment.Comment
import yjh.ontongsal.api.common.domain.Actor
import yjh.ontongsal.api.common.exception.AppException
import yjh.ontongsal.api.common.exception.ErrorCode

@Service
class CommentCommandService(
    private val loadArticlePort: LoadArticlePort,
    private val saveCommentPort: SaveCommentPort,
    private val deleteCommentPort: DeleteCommentPort,
    private val eventPublisher: EventPublisher,
) : WriteCommentUseCase, UpdateCommentUseCase, DeleteCommentUseCase {

    override fun write(command: CommentWriteCommand): Long {
        val article: Article = loadArticlePort.findById(command.articleId)
            ?: throw AppException.NotFound(ErrorCode.ARTICLE_NOT_FOUND, "Article ${command.articleId} not found")

        val actor = Actor(command.actorId, command.actorName)
        val comment = Comment.create(article, actor, command.content)
        val saved = saveCommentPort.create(comment)

        val event = CommentCreatedEvent(
            articleId = command.articleId,
            commentId = saved.id
        )
        eventPublisher.publish(event)

        return saved.id
    }

    override fun update(command: CommentUpdateCommand): Long {
        TODO("Not yet implemented")
    }

    override fun delete(commentDeleteCommand: CommentDeleteCommand) {
        TODO("Not yet implemented")
    }
}
