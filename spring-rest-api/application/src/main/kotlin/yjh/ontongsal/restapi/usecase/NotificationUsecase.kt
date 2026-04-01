package yjh.ontongsal.restapi.usecase

import yjh.ontongsal.restapi.CommentCreatedEvent

interface NotificationUsecase {
    fun alert(event: CommentCreatedEvent): Unit
}
