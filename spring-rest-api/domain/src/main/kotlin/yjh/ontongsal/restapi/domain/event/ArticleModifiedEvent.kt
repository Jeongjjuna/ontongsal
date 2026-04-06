package yjh.ontongsal.restapi.domain.event

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.TargetId
import java.time.Instant
import java.util.*

data class ArticleModifiedEvent(
    val articleId: Long,
    val writerId: Long,
    val writerName: String,
    override val occurredAt: Instant = Instant.now(),
    override val eventId: String = UUID.randomUUID().toString(),
) : DomainEvent {

    companion object {
        fun of(actor: Actor, articleId: TargetId, @Suppress("UNUSED_PARAMETER") article: Article): ArticleModifiedEvent {
            return ArticleModifiedEvent(
                articleId = articleId.targetId,
                writerId = actor.userId,
                writerName = actor.userName,
            )
        }
    }
}
