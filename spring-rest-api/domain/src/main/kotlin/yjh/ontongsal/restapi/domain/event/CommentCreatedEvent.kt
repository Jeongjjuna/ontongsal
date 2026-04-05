package yjh.ontongsal.restapi.domain.event

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Comment
import yjh.ontongsal.restapi.domain.TargetId
import java.time.Instant
import java.util.*

data class CommentCreatedEvent(
    val articleId: Long,
    val commentId: Long,
    val writerId: Long,
    val writerName: String,
    override val occurredAt: Instant = Instant.now(),
    override val eventId: String = UUID.randomUUID().toString(),
) : DomainEvent {

    companion object {
        fun of(actor: Actor, targetId: TargetId, comment: Comment): CommentCreatedEvent {
            return CommentCreatedEvent(
                articleId = targetId.targetId,
                commentId = comment.id,
                writerId = actor.userId,
                writerName = actor.userName
            )
        }
    }
}
