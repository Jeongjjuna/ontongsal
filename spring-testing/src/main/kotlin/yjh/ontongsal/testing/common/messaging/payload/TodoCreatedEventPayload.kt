package yjh.ontongsal.testing.common.messaging.payload

import yjh.ontongsal.testing.common.messaging.EventPayload

data class TodoCreatedEventPayload(
    val todoId: Long,
    val userId: Long,
    val title: String,
    val content: String,
) : EventPayload
