package wemade.ontongsal.springmodulith.event.domain.event

import wemade.ontongsal.springmodulith.event.domain.RewardType
import java.time.LocalDateTime

data class AttendanceItem(
    val id: Long,
    val day: Int,
    val reward: RewardType,
)

// 2. 출석 체크 이벤트
class AttendanceEvent(
    override val id: Long,
    override val name: String,
    override val isActive: Boolean,
    override val startedAt: LocalDateTime,
    override val endedAt: LocalDateTime,
    val attendances: List<AttendanceItem>,
) : Event() {

    fun findItem(itemId: Long): AttendanceItem? = attendances.firstOrNull { it.id == itemId }

    // 같은 이벤트 내에서 해당 day 이전(day < target.day) 인 출석 항목들.
    fun previousItems(day: Int): List<AttendanceItem> =
        attendances.filter { it.day < day }
}



