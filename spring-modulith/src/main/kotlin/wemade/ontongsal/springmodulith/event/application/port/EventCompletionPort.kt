package wemade.ontongsal.springmodulith.event.application.port

interface EventCompletionPort {

    // 주어진 eventIds(자식 event id) 중 userId 가 이미 완료한 id 집합을 반환.
    fun findCompletedEventIds(userId: Long, eventIds: List<Long>): Set<Long>

    // 완료 이력을 저장하고, EVENT_COMPLETED 메시지 멱등성 키로 쓸 transaction_id 를 반환.
    fun save(userId: Long, eventId: Long): Long

    // 보상 수령 시각을 기록. 이미 기록된 경우엔 덮어쓰지 않는다.
    fun markRewarded(userId: Long, eventId: Long)
}
