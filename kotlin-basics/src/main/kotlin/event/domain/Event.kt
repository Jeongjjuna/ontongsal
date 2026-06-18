package event.domain

/**
 * Event <-> Policy 를 연결한다(타입 안전하게)
 *
 * Mission 이벤트는 MissionEventPolicy 만 가진다.
 * 컴포지션도 좋지만, 직접 타입 안전하게 제어하기 위해 제너릭 out 을 사용한다.
 */
sealed interface Event<out P : EventPolicy> {

    data object Mission : Event<EventPolicy.MissionEventPolicy>

    data object Attendance : Event<EventPolicy.AttendanceEventPolicy>

    data object Invite : Event<EventPolicy.InvitationEventPolicy>
}