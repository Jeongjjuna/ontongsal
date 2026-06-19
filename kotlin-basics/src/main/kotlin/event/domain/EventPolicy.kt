package event.domain

sealed interface EventPolicy {

    sealed interface MissionEventPolicy : EventPolicy {
        fun isSatisfied(context: MissionContext): Boolean

        /** 참여 버튼만 누르면 완료 */
        data object ManualParticipation : MissionEventPolicy {
            override fun isSatisfied(context: MissionContext): Boolean = true
        }

        /** 특정 게임 레벨 달성 시 완료 */
        data class ReachGameLevel(val level: Int) : MissionEventPolicy {
            override fun isSatisfied(context: MissionContext): Boolean {
                return (context.userLevel ?: 0) >= level
            }
        }

        /** 외부 시스템 사전예약 성공 시 완료 */
        data object PreRegistrationCompleted : MissionEventPolicy {
            override fun isSatisfied(context: MissionContext): Boolean {
                return context.hasPreRegistered
            }
        }
    }

    data class MissionContext(
        val userLevel: Int? = null,
        val hasPreRegistered: Boolean = false,
    )

    sealed interface AttendanceEventPolicy : EventPolicy {
        fun isSatisfied(context: AttendanceContext): Boolean

        /** 하루 출석 */
        data object DailyAttendance : AttendanceEventPolicy {
            override fun isSatisfied(context: AttendanceContext): Boolean {
                return context.attendanceCount >= 1
            }
        }

        /** 주간 출석 */
        data object WeeklyAttendance : AttendanceEventPolicy {
            override fun isSatisfied(context: AttendanceContext): Boolean {
                return context.attendanceCount >= 7
            }
        }

        /** 연속 출석 */
        data class ConsecutiveAttendance(val days: Int) : AttendanceEventPolicy {
            override fun isSatisfied(context: AttendanceContext): Boolean {
                return context.consecutiveDays >= days
            }
        }
    }

    data class AttendanceContext(
        val attendanceCount: Int = 0,
        val consecutiveDays: Int = 0,
    )

    sealed interface InvitationEventPolicy : EventPolicy {
        fun isSatisfied(context: InvitationContext): Boolean

        /** 초대 코드로 참여 시 완료 */
        data object JoinWithInvitationCode : InvitationEventPolicy {
            override fun isSatisfied(context: InvitationContext): Boolean {
                return context.hasUsedInviteCode
            }
        }
    }

    data class InvitationContext(
        val hasUsedInviteCode: Boolean = false,
    )
}