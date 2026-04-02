package yjh.ontongsal.restapi

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.event.CommentCreatedEvent
import yjh.ontongsal.restapi.event.CommentModifiedEvent
import yjh.ontongsal.restapi.port.EventPublisher

private val logger = KotlinLogging.logger { }

@Component
class CommentEventHandler(
    private val eventPublisher: EventPublisher,
) {

    fun created(actor: Actor, articleId: TargetId, comment: Comment) {
        eventPublisher.publish(CommentCreatedEvent.of(actor, articleId, comment))
        logger.debug { "comment created event published : commentId=${comment.id}" }
    }

    fun updated(actor: Actor, commentId: TargetId, comment: Comment) {
        val event =
        eventPublisher.publish(CommentModifiedEvent.of(actor, commentId, comment))
        logger.debug { "comment created event published : commentId=${comment.id}" }
    }
}
