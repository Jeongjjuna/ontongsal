package yjh.ontongsal.testing.common.event.payload

import yjh.ontongsal.testing.common.event.EventPayload

data class TodoCreatedEventPayload(
    val todoId: Long,
    val userId: Long,
    val title: String,
    val content: String,
) : EventPayload
