package wemade.ontongsal.springmodulith.event.presentation

import wemade.ontongsal.springmodulith.event.application.port.CreateChildEventCommand
import wemade.ontongsal.springmodulith.event.application.port.CreateParentEventCommand
import wemade.ontongsal.springmodulith.event.application.port.UpdateChildEventCommand
import wemade.ontongsal.springmodulith.event.application.port.UpdateParentEventCommand
import wemade.ontongsal.springmodulith.event.domain.RewardType
import wemade.ontongsal.springmodulith.event.domain.event.CompletionCondition
import wemade.ontongsal.springmodulith.event.domain.event.EventType
import java.time.LocalDateTime

data class CreateParentEventRequest(
    val type: EventType,
    val name: String,
    val isActive: Boolean = true,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
) {
    fun toCommand() = CreateParentEventCommand(type, name, isActive, startedAt, endedAt)
}

data class CreateEventWithItemsRequest(
    val type: EventType,
    val name: String,
    val isActive: Boolean = true,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val items: List<CreateChildEventRequest> = emptyList(),
) {
    fun toParentCommand() = CreateParentEventCommand(type, name, isActive, startedAt, endedAt)
    fun toChildCommands() = items.map { it.toCommand() }
}

data class UpdateParentEventRequest(
    val name: String,
    val isActive: Boolean,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
) {
    fun toCommand() = UpdateParentEventCommand(name, isActive, startedAt, endedAt)
}

data class CreateChildEventRequest(
    val name: String,
    val reward: RewardType,
    val completionCondition: CompletionCondition? = null,
    val day: Int? = null,
) {
    fun toCommand() = CreateChildEventCommand(name, reward, completionCondition, day)
}

data class UpdateChildEventRequest(
    val name: String,
    val reward: RewardType,
    val completionCondition: CompletionCondition? = null,
    val day: Int? = null,
) {
    fun toCommand() = UpdateChildEventCommand(name, reward, completionCondition, day)
}
