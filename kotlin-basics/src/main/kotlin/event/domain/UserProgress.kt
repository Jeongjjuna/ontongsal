package event.domain

data class UserProgress(
    val attendanceCount: Int = 0,
    val consecutiveAttendanceDays: Int = 0,
)