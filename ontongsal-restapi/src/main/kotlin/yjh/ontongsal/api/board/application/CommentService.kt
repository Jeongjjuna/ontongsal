package yjh.ontongsal.api.board.application

import org.springframework.stereotype.Service
import yjh.ontongsal.api.board.application.input.CommentCreateCommand
import yjh.ontongsal.api.board.application.input.CommentUsecase
import yjh.ontongsal.api.board.application.output.CommentRepositoryPort
import yjh.ontongsal.api.board.application.output.EventPublisher
import yjh.ontongsal.api.board.application.output.PostRepositoryPort
import yjh.ontongsal.api.board.domain.Actor
import yjh.ontongsal.api.board.domain.CommentCreatedEvent
import yjh.ontongsal.api.board.domain.comment.Comment
import yjh.ontongsal.api.board.domain.post.Post
import yjh.ontongsal.api.common.exception.AppException
import yjh.ontongsal.api.common.exception.ErrorCode

@Service
class CommentService(
    private val commentRepositoryPort: CommentRepositoryPort,
    private val postRepositoryPort: PostRepositoryPort,
    private val eventPublisher: EventPublisher,
) : CommentUsecase {

    override fun write(command: CommentCreateCommand): Comment {
        val post: Post = postRepositoryPort.findById(command.postId)
            ?: throw AppException.NotFound(ErrorCode.POST_NOT_FOUND, "Post ${command.postId} not found")

        val actor = Actor(command.actorId, command.actorName)
        val comment = Comment.create(post, actor, command.content)
        return commentRepositoryPort.save(comment)
            .also { savedComment ->
                val event = CommentCreatedEvent(
                    postId = command.postId,
                    commentId = savedComment.id
                )
                eventPublisher.publish(event)
            }
    }

    override fun read(commentId: Long) = commentRepositoryPort.findById(commentId)
        ?: throw AppException.NotFound(ErrorCode.COMMENT_NOT_FOUND, "Comment $commentId not found")
}
