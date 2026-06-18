package event.application

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

class MissionEventProcessor : EventPolicyProcessor<EventPolicy.MissionEventPolicy> {
    override fun process(userId: String, policy: EventPolicy.MissionEventPolicy): EventResult {
        return when (policy) {
            is ManualParticipation -> {
                // TODO
                EventResult.Success(userId, Instant.now())
            }

            is ReachGameLevel -> {
                // TODO
                EventResult.Success(userId, Instant.now())
            }

            is PreRegistrationCompleted -> {
                // TODO
                EventResult.Success(userId, Instant.now())
            }
        }
    }
}

class AttendanceEventProcessor : EventPolicyProcessor<EventPolicy.AttendanceEventPolicy> {
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

class InvitationEventProcessor : EventPolicyProcessor<EventPolicy.InvitationEventPolicy> {
    override fun process(userId: String, policy: EventPolicy.InvitationEventPolicy): EventResult {
        return when (policy) {
            is JoinWithInvitationCode -> {
                // TODO
                EventResult.Success(userId, Instant.now())
            }
        }
    }

}