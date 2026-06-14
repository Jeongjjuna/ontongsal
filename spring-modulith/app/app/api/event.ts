// EventController — RequestMapping("/api/events") 응답/요청 타입.
import type { CompletionCondition, EventStatus, EventType, IsoDateTime, RewardType } from './types'

export type MissionItemResponse = {
  id: number
  name: string
  completionCondition: CompletionCondition
  reward: RewardType
}

export type AttendanceItemResponse = {
  id: number
  day: number
  reward: RewardType
}

export type EventResponse = {
  id: number
  name: string
  type: EventType
  status: EventStatus
  isActive: boolean
  startedAt: IsoDateTime
  endedAt: IsoDateTime
  missions?: MissionItemResponse[]
  attendances?: AttendanceItemResponse[]
}
