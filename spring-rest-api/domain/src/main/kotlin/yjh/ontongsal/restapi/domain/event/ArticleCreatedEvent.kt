package yjh.ontongsal.restapi.domain.event

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import java.time.Instant
import java.util.*

data class ArticleCreatedEvent(
    val boardId: Long,
    val articleId: Long,
    val writerId: Long,
    val writerName: String,
    override val occurredAt: Instant = Instant.now(),
    override val eventId: String = UUID.randomUUID().toString(),
) : DomainEvent {

    companion object {
        fun of(actor: Actor, article: Article): ArticleCreatedEvent {
            return ArticleCreatedEvent(
                boardId = article.boardId,
                articleId = article.id,
                writerId = actor.userId,
                writerName = actor.userName,
            )
        }
    }
}
