package wemade.ontongsal.springmodulith.event.infrastructure

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.datetime

object EventCompletionTable : Table("event_completion") {
    val userId = long("user_id")
    val eventId = long("event_id") // 자식 event id (MissionItem / AttendanceItem id)
    val completedAt = datetime("completed_at")
    val rewardReceivedAt = datetime("reward_received_at").nullable()
    val transactionId = long("transaction_id").autoIncrement().uniqueIndex()

    override val primaryKey = PrimaryKey(userId, eventId)
}
