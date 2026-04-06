package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.EventPublisher
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.event.ArticleCreatedEvent
import yjh.ontongsal.restapi.domain.event.ArticleModifiedEvent

private val logger = KotlinLogging.logger { }

@Component
class ArticleEventHandler(
    private val eventPublisher: EventPublisher,
) {

    fun created(actor: Actor, article: Article) {
        eventPublisher.publish(ArticleCreatedEvent.of(actor, article))
        logger.debug { "article created event published : articleId=${article.id}" }
    }

    fun updated(actor: Actor, articleId: TargetId, article: Article) {
        eventPublisher.publish(ArticleModifiedEvent.of(actor, articleId, article))
        logger.debug { "article modified event published : articleId=${article.id}" }
    }
}
