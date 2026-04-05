package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.EventPublisher
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Comment
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.event.CommentCreatedEvent
import yjh.ontongsal.restapi.domain.event.CommentModifiedEvent

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
