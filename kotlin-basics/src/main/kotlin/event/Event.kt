package event


sealed interface Event {

    data object Mission : Event

    data object Attendance : Event

    data object Invite : Event
}