package yjh.ontongsal.api.board.domain

import java.time.Instant
import java.util.UUID

interface DomainEvent {
    val occurredAt: Instant
    val eventId: String
}

// 댓글 작성 이벤트
data class CommentCreatedEvent(
    val postId: Long,
    val commentId: Long,
    override val occurredAt: Instant = Instant.now(),
    override val eventId: String = UUID.randomUUID().toString(),
) : DomainEvent
