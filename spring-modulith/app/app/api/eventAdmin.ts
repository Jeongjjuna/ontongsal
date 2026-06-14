// EventAdminController — RequestMapping("/api/admin/events") 응답/요청 타입.
import type { CompletionCondition, EventType, IsoDateTime, RewardType } from './types'

export type EventMasterRowResponse = {
  eventId: number
  parentEventId: number
  type: EventType
  name: string
  isActive: boolean
  startedAt: IsoDateTime
  endedAt: IsoDateTime
  completionCondition: CompletionCondition | null
  reward: RewardType | null
  day: number | null
}

export type EventMasterTreeResponse = {
  parent: EventMasterRowResponse
  children: EventMasterRowResponse[]
}

export type CreateParentEventRequest = {
  type: EventType
  name: string
  isActive?: boolean
  startedAt: IsoDateTime
  endedAt: IsoDateTime
}

export type CreateChildEventRequest = {
  name: string
  reward: RewardType
  completionCondition?: CompletionCondition | null
  day?: number | null
}

export type CreateEventWithItemsRequest = CreateParentEventRequest & {
  items?: CreateChildEventRequest[]
}

export type UpdateParentEventRequest = {
  name: string
  isActive: boolean
  startedAt: IsoDateTime
  endedAt: IsoDateTime
}

export type UpdateChildEventRequest = CreateChildEventRequest

export type CreatedEventId = { eventId: number }
