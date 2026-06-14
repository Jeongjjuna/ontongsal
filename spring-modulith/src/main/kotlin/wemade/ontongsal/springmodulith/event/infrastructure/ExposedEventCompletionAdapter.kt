package wemade.ontongsal.springmodulith.event.infrastructure

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.stereotype.Repository
import wemade.ontongsal.springmodulith.event.application.port.EventCompletionPort
import wemade.ontongsal.springmodulith.shared.transation.TransactionRunner
import java.time.LocalDateTime

@Repository
class ExposedEventCompletionAdapter(
    private val transaction: TransactionRunner,
) : EventCompletionPort {

    override fun findCompletedEventIds(userId: Long, eventIds: List<Long>): Set<Long> = transaction.run {
        if (eventIds.isEmpty()) return@run emptySet()

        EventCompletionTable
            .select(EventCompletionTable.eventId)
            .where {
                (EventCompletionTable.userId eq userId) and
                    (EventCompletionTable.eventId inList eventIds)
            }
            .map { it[EventCompletionTable.eventId] }
            .toSet()
    }

    override fun save(userId: Long, eventId: Long): Long = transaction.run {
        val statement = EventCompletionTable.insert {
            it[EventCompletionTable.userId] = userId
            it[EventCompletionTable.eventId] = eventId
            it[completedAt] = LocalDateTime.now()
        }
        statement[EventCompletionTable.transactionId]
    }

    override fun markRewarded(userId: Long, eventId: Long) {
        transaction.run {
            EventCompletionTable.update({
                (EventCompletionTable.userId eq userId) and
                    (EventCompletionTable.eventId eq eventId) and
                    EventCompletionTable.rewardReceivedAt.isNull()
            }) {
                it[rewardReceivedAt] = LocalDateTime.now()
            }
        }
    }
}
