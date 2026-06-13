package wemade.ontongsal.springmodulith.event.domain.event

import wemade.ontongsal.springmodulith.event.domain.RewardType
import java.time.LocalDateTime

data class MissionItem(
    val id: Long,
    val name: String,
    val completionCondition: CompletionCondition,
    val reward: RewardType,
)

// 미션 달성(완료) 조건. 어떤 행동을 했을 때 미션이 완료되는지 결정한다.
enum class CompletionCondition {
    CLICK,               // 완료 버튼 클릭(단순 체크)
    PRE_REGISTER         // 사전 예약 완료
}

// 1. 미션 달성 이벤트
class MissionEvent(
    override val id: Long,
    override val name: String,
    override val isActive: Boolean,
    override val startedAt: LocalDateTime,
    override val endedAt: LocalDateTime,
    val missions: List<MissionItem>,
) : Event() {

    fun findItem(itemId: Long): MissionItem? = missions.firstOrNull { it.id == itemId }

    // 미션 순서는 missions 리스트의 인덱스로 정의된다.
    fun previousItems(itemId: Long): List<MissionItem> {
        val idx = missions.indexOfFirst { it.id == itemId }
        return if (idx <= 0) emptyList() else missions.subList(0, idx)
    }
}
