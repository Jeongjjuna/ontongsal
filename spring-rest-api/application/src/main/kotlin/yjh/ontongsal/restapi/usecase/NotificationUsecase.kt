package yjh.ontongsal.restapi.usecase

import yjh.ontongsal.restapi.event.CommentCreatedEvent

interface NotificationUsecase {
    fun alert(event: CommentCreatedEvent): Unit
}
