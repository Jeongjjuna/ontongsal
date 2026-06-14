// Spring Boot 도메인 enum 미러링. Kotlin enum 의 이름과 그대로 매칭된다.

export type EventStatus = 'BEFORE' | 'ONGOING' | 'ENDED'

export type EventType = 'MISSION' | 'ATTENDANCE'

export type RewardType = 'POINT' | 'ITEM'

export type CompletionCondition = 'CLICK' | 'PRE_REGISTER'

// Jackson JSR-310 직렬화 결과: ISO-8601 문자열.
export type IsoDateTime = string
