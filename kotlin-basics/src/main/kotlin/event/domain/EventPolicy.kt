package event.domain

sealed interface EventPolicy {

    sealed interface MissionEventPolicy : EventPolicy {

        /** 참여 버튼만 누르면 완료 */
        data object ManualParticipation : MissionEventPolicy

        /** 특정 게임 레벨 달성 시 완료 */
        data class ReachGameLevel(val level: Int) : MissionEventPolicy

        /** 외부 시스템 사전예약 성공 시 완료 */
        data class PreRegistrationCompleted(val userId: String) : MissionEventPolicy
    }

    sealed interface AttendanceEventPolicy : EventPolicy {

        /** 하루 출석 */
        data object DailyAttendance : AttendanceEventPolicy

        /** 주간 출석 */
        data object WeeklyAttendance : AttendanceEventPolicy

        /** 연속 출석 */
        data class ConsecutiveAttendance(val days: Int) : AttendanceEventPolicy
    }

    sealed interface InvitationEventPolicy : EventPolicy {

        /** 초대 코드로 참여 시 완료 */
        data object JoinWithInvitationCode : InvitationEventPolicy
    }
}