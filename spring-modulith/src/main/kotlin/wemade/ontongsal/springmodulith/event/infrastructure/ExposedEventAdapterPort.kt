package wemade.ontongsal.springmodulith.event.infrastructure

import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.stereotype.Component
import wemade.ontongsal.springmodulith.event.application.port.CreateChildEventCommand
import wemade.ontongsal.springmodulith.event.application.port.CreateParentEventCommand
import wemade.ontongsal.springmodulith.event.application.port.EventMasterRow
import wemade.ontongsal.springmodulith.event.application.port.EventRepositoryPort
import wemade.ontongsal.springmodulith.event.application.port.UpdateChildEventCommand
import wemade.ontongsal.springmodulith.event.application.port.UpdateParentEventCommand
import wemade.ontongsal.springmodulith.event.domain.event.EventStatus
import wemade.ontongsal.springmodulith.event.domain.RewardType
import wemade.ontongsal.springmodulith.event.domain.event.*
import wemade.ontongsal.springmodulith.shared.TransactionRunner
import java.time.LocalDateTime

@Component
class ExposedEventAdapterPort(
    private val transaction: TransactionRunner,
) : EventRepositoryPort {


    override fun findParentAndChildren(eventId: Long): Event? = transaction.run {

        // 인자가 자식 id 일 수 있으므로 먼저 자기 행을 찾아 실제 부모 id 를 결정한다.
        val selfRow = EventMasterTable
            .selectAll()
            .where { EventMasterTable.eventId eq eventId }
            .singleOrNull()
            ?: return@run null

        val parentId = selfRow[EventMasterTable.parentEventId]
            .takeIf { it != 0L && it != eventId }
            ?: eventId

        val rows = EventMasterTable
            .selectAll()
            .where {
                (EventMasterTable.eventId eq parentId) or
                    (EventMasterTable.parentEventId eq parentId)
            }
            .toList()

        if (rows.isEmpty()) {
            return@run null
        }

        val parent = rows.firstOrNull {
            it[EventMasterTable.eventId] == parentId
        } ?: return@run null

        val children = rows.filter {
            it[EventMasterTable.parentEventId] == parentId
        }

        return@run buildEvent(parent, children)
    }

    override fun findAllByStatuses(statuses: Set<EventStatus>): List<Event> = transaction.run {
        if (statuses.isEmpty()) return@run emptyList()
        val now = LocalDateTime.now()

        // 각 상태별 조건을 OR 로 합친다.
        val statusOps = statuses.map { status ->
            when (status) {
                EventStatus.BEFORE -> EventMasterTable.startedAt greater now
                EventStatus.ONGOING ->
                    (EventMasterTable.startedAt lessEq now) and (EventMasterTable.endedAt greaterEq now)
                EventStatus.ENDED -> EventMasterTable.endedAt less now
            }
        }
        val statusCondition = statusOps.reduce { acc, op -> acc or op }

        // 부모는 parentEventId = 0 으로 마킹된다. isActive=false 는 유저 API 노출 제외.
        val parents = EventMasterTable
            .selectAll()
            .where {
                (EventMasterTable.parentEventId eq 0L) and
                    (EventMasterTable.isActive eq true) and
                    statusCondition
            }
            .toList()

        return@run hydrateParents(parents)
    }

    override fun findAll(): List<Event> = transaction.run {
        val parents = EventMasterTable
            .selectAll()
            .where { EventMasterTable.parentEventId eq 0L }
            .toList()

        return@run hydrateParents(parents)
    }

    override fun findAllMasterRows(): List<EventMasterRow> = transaction.run {
        EventMasterTable
            .selectAll()
            .map {
                EventMasterRow(
                    eventId = it[EventMasterTable.eventId],
                    parentEventId = it[EventMasterTable.parentEventId],
                    type = it[EventMasterTable.type],
                    name = it[EventMasterTable.name],
                    isActive = it[EventMasterTable.isActive],
                    startedAt = it[EventMasterTable.startedAt],
                    endedAt = it[EventMasterTable.endedAt],
                    completionCondition = it[EventMasterTable.completionCondition],
                    reward = it[EventMasterTable.reward],
                    day = it[EventMasterTable.day],
                )
            }
    }

    override fun createParent(command: CreateParentEventCommand): Long = transaction.run {
        EventMasterTable.insert {
            it[parentEventId] = 0L
            it[type] = command.type
            it[name] = command.name
            it[isActive] = command.isActive
            it[startedAt] = command.startedAt
            it[endedAt] = command.endedAt
        } get EventMasterTable.eventId
    }

    override fun createChild(parentId: Long, command: CreateChildEventCommand): Long = transaction.run {
        // startedAt/endedAt 컬럼은 NOT NULL 이지만 도메인상 자식은 부모 기간에 종속. 부모 값을 그대로 복사한다.
        val parentRow = EventMasterTable
            .selectAll()
            .where { EventMasterTable.eventId eq parentId }
            .single()

        EventMasterTable.insert {
            it[parentEventId] = parentId
            it[type] = parentRow[EventMasterTable.type]
            it[name] = command.name
            it[isActive] = parentRow[EventMasterTable.isActive]
            it[startedAt] = parentRow[EventMasterTable.startedAt]
            it[endedAt] = parentRow[EventMasterTable.endedAt]
            it[completionCondition] = command.completionCondition
            it[reward] = command.reward
            it[day] = command.day
        } get EventMasterTable.eventId
    }

    override fun updateParent(eventId: Long, command: UpdateParentEventCommand) {
        transaction.run {
            EventMasterTable.update({ EventMasterTable.eventId eq eventId }) {
                it[name] = command.name
                it[isActive] = command.isActive
                it[startedAt] = command.startedAt
                it[endedAt] = command.endedAt
            }
        }
    }

    override fun updateChild(eventId: Long, command: UpdateChildEventCommand) {
        transaction.run {
            EventMasterTable.update({ EventMasterTable.eventId eq eventId }) {
                it[name] = command.name
                it[reward] = command.reward
                it[completionCondition] = command.completionCondition
                it[day] = command.day
            }
        }
    }

    override fun deleteParentCascade(parentId: Long) {
        transaction.run {
            EventMasterTable.deleteWhere {
                (EventMasterTable.eventId eq parentId) or
                    (EventMasterTable.parentEventId eq parentId)
            }
        }
    }

    override fun deleteChild(eventId: Long) {
        transaction.run {
            EventMasterTable.deleteWhere { EventMasterTable.eventId eq eventId }
        }
    }

    private fun hydrateParents(parents: List<ResultRow>): List<Event> {
        if (parents.isEmpty()) return emptyList()
        val parentIds = parents.map { it[EventMasterTable.eventId] }
        val childrenByParent = EventMasterTable
            .selectAll()
            .where { EventMasterTable.parentEventId inList parentIds }
            .toList()
            .groupBy { it[EventMasterTable.parentEventId] }

        return parents.map { parent ->
            val pid = parent[EventMasterTable.eventId]
            buildEvent(parent, childrenByParent[pid].orEmpty())
        }
    }

    private fun buildEvent(parent: ResultRow, children: List<ResultRow>): Event =
        when (parent[EventMasterTable.type]) {

            EventType.MISSION ->
                MissionEvent(
                    id = parent[EventMasterTable.eventId],
                    name = parent[EventMasterTable.name],
                    isActive = parent[EventMasterTable.isActive],
                    startedAt = parent[EventMasterTable.startedAt],
                    endedAt = parent[EventMasterTable.endedAt],
                    missions = children
                        .sortedBy { it[EventMasterTable.eventId] }
                        .map {
                            MissionItem(
                                id = it[EventMasterTable.eventId],
                                name = it[EventMasterTable.name],
                                completionCondition = it[EventMasterTable.completionCondition] ?: CompletionCondition.CLICK,
                                reward = it[EventMasterTable.reward] ?: RewardType.POINT,
                            )
                        }
                )

            EventType.ATTENDANCE ->
                AttendanceEvent(
                    id = parent[EventMasterTable.eventId],
                    name = parent[EventMasterTable.name],
                    isActive = parent[EventMasterTable.isActive],
                    startedAt = parent[EventMasterTable.startedAt],
                    endedAt = parent[EventMasterTable.endedAt],
                    attendances = children
                        .sortedBy { it[EventMasterTable.day] ?: 0 }
                        .map { row ->
                            AttendanceItem(
                                id = row[EventMasterTable.eventId],
                                day = row[EventMasterTable.day] ?: 0,
                                reward = row[EventMasterTable.reward] ?: RewardType.POINT,
                            )
                        }
                )
        }
}
