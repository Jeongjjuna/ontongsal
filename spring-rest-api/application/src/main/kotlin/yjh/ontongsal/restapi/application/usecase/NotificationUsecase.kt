package yjh.ontongsal.restapi.application.usecase

import yjh.ontongsal.restapi.domain.event.CommentCreatedEvent

interface NotificationUsecase {
    fun alert(event: CommentCreatedEvent): Unit
}
