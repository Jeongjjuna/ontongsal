package event.application

import event.application.port.InviteCodeReader
import event.application.port.PreRegistrationClient
import event.application.port.UserLevelReader
import event.application.port.UserProgressReader
import event.domain.EventPolicy
import event.domain.EventPolicy.MissionEventPolicy.ManualParticipation
import event.domain.EventPolicy.MissionEventPolicy.ReachGameLevel
import event.domain.EventPolicy.MissionEventPolicy.PreRegistrationCompleted
import event.domain.EventPolicy.AttendanceEventPolicy.DailyAttendance
import event.domain.EventPolicy.AttendanceEventPolicy.WeeklyAttendance
import event.domain.EventPolicy.AttendanceEventPolicy.ConsecutiveAttendance
import event.domain.EventPolicy.InvitationEventPolicy.JoinWithInvitationCode
import event.domain.EventResult
import java.time.Instant


interface EventPolicyProcessor<in P : EventPolicy> {
    fun process(userId: String, policy: P): EventResult
}

class MissionEventProcessor(
    private val userLevelReader: UserLevelReader,
    private val preRegistrationClient: PreRegistrationClient,
) : EventPolicyProcessor<EventPolicy.MissionEventPolicy> {

    override fun process(userId: String, policy: EventPolicy.MissionEventPolicy): EventResult {
        return when (policy) {
            is ManualParticipation -> {
                EventResult.Success(userId, Instant.now())
            }

            is ReachGameLevel -> {
                val currentLevel = userLevelReader.getUserLevel(userId)
                if (currentLevel >= policy.level) {
                    EventResult.Success(userId, Instant.now())
                } else {
                    EventResult.Failure.ConditionNotMet
                }
            }

            is PreRegistrationCompleted -> {
                if (preRegistrationClient.hasCompleted(userId)) {
                    EventResult.Success(userId, Instant.now())
                } else {
                    EventResult.Failure.ConditionNotMet
                }
            }
        }
    }
}

class AttendanceEventProcessor(
    private val userProgressReader: UserProgressReader,
) : EventPolicyProcessor<EventPolicy.AttendanceEventPolicy> {

    override fun process(userId: String, policy: EventPolicy.AttendanceEventPolicy): EventResult {
        return when (policy) {
            is DailyAttendance -> {
                // TODO
                EventResult.Success(userId, Instant.now())
            }

            is ConsecutiveAttendance -> {
                // TODO
                EventResult.Success(userId, Instant.now())
            }

            is WeeklyAttendance -> {
                // TODO
                EventResult.Success(userId, Instant.now())
            }
        }
    }
}

class InvitationEventProcessor(
    private val inviteCodeReader: InviteCodeReader,
) : EventPolicyProcessor<EventPolicy.InvitationEventPolicy> {

    override fun process(userId: String, policy: EventPolicy.InvitationEventPolicy): EventResult {
        return when (policy) {
            is JoinWithInvitationCode -> {
                // TODO
                EventResult.Success(userId, Instant.now())
            }
        }
    }

}