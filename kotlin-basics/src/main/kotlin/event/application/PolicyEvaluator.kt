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


interface PolicyEvaluator<in P : EventPolicy> {
    fun evaluate(userId: String, policy: P): EventResult
}

class MissionEvaluator(
    private val userLevelReader: UserLevelReader,
    private val preRegistrationClient: PreRegistrationClient,
) : PolicyEvaluator<EventPolicy.MissionEventPolicy> {

    override fun evaluate(userId: String, policy: EventPolicy.MissionEventPolicy): EventResult {
        val context = EventPolicy.MissionContext(
            userLevel = if (policy is ReachGameLevel) userLevelReader.getUserLevel(userId) else null,
            hasPreRegistered = if (policy is PreRegistrationCompleted) preRegistrationClient.hasCompleted(userId) else false
        )

        return if (policy.isSatisfied(context)) {
            EventResult.Success(userId, Instant.now())
        } else {
            EventResult.Failure.ConditionNotMet
        }
    }
}

class AttendanceEvaluator(
    private val userProgressReader: UserProgressReader,
) : PolicyEvaluator<EventPolicy.AttendanceEventPolicy> {

    override fun evaluate(userId: String, policy: EventPolicy.AttendanceEventPolicy): EventResult {
        val progress = userProgressReader.getEventProgress(userId)
        val context = EventPolicy.AttendanceContext(
            attendanceCount = progress.attendanceCount,
            consecutiveDays = progress.consecutiveAttendanceDays
        )

        return if (policy.isSatisfied(context)) {
            EventResult.Success(userId, Instant.now())
        } else {
            EventResult.Failure.ConditionNotMet
        }
    }
}

class InvitationEvaluator(
    private val inviteCodeReader: InviteCodeReader,
) : PolicyEvaluator<EventPolicy.InvitationEventPolicy> {

    override fun evaluate(userId: String, policy: EventPolicy.InvitationEventPolicy): EventResult {
        val context = EventPolicy.InvitationContext(
            hasUsedInviteCode = inviteCodeReader.hasUsedCode(userId)
        )

        return if (policy.isSatisfied(context)) {
            EventResult.Success(userId, Instant.now())
        } else {
            EventResult.Failure.ConditionNotMet
        }
    }
}