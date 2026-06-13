package wemade.ontongsal.springmodulith.event.infrastructure

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.datetime
import wemade.ontongsal.springmodulith.event.domain.RewardType
import wemade.ontongsal.springmodulith.event.domain.event.CompletionCondition
import wemade.ontongsal.springmodulith.event.domain.event.EventType

object EventMasterTable : Table("eventMaster") {
    val eventId = long("eventId").autoIncrement()
    val parentEventId = long("parent_event_id") // 부모는 0L 로 마킹
    val type = enumerationByName(name = "type", length = 20, klass = EventType::class)
    val name = varchar(name = "name", length = 100)
    val isActive = bool("is_active")
    val startedAt = datetime("started_at")
    val endedAt = datetime("ended_at")

    // 자식 전용 컬럼들 (부모는 null)
    val completionCondition = enumerationByName("completion_condition", 20, CompletionCondition::class).nullable()
    val reward = enumerationByName("reward", 20, RewardType::class).nullable()
    val day = integer("day").nullable()

    override val primaryKey = PrimaryKey(eventId)
}
