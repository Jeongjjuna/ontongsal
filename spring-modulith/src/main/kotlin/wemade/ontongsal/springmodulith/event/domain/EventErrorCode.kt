package wemade.ontongsal.springmodulith.event.domain

import wemade.ontongsal.springmodulith.shared.ErrorCode

enum class EventErrorCode(
    override val code: Int,
    override val message: String,
) : ErrorCode {

    EVENT_NOT_FOUND(5000, "이벤트를 찾을 수 없습니다"),
    EVENT_NOT_ACTIVE(5001, "진행 중인 이벤트가 아닙니다"),
    EVENT_EXPIRED(5002, "종료된 이벤트입니다"),
    EVENT_NOT_STARTED(5003, "아직 시작되지 않은 이벤트입니다"),
    EVENT_ALREADY_COMPLETED(5004, "이미 완료한 이벤트입니다"),
    EVENT_CONDITION_NOT_MET(5005, "이벤트 달성 조건을 만족하지 않습니다"),
    EVENT_REWARD_ALREADY_CLAIMED(5006, "이미 보상을 수령했습니다"),
    EVENT_REWARD_SEND_FAILED(5007, "보상 지급에 실패했습니다"),
    EVENT_LOCKED(5008, "이미 시작된 이벤트는 수정·삭제할 수 없습니다"),
}
