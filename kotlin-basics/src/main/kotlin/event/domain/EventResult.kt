package event.domain

import java.time.Instant

sealed interface EventResult {
    data class Success(val userId: String, val completedAt: Instant) : EventResult

    sealed interface Failure : EventResult {
        data object AlreadyCompleted: Failure
        data object ConditionNotMet: Failure
    }
}