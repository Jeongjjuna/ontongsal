package yjh.ontongsal.api.notification.application.input

import yjh.ontongsal.api.board.domain.CommentCreatedEvent

interface NotificationUsecase {
    fun alert(event: CommentCreatedEvent): Unit
}
