package yjh.ontongsal.api.notification.application

import org.springframework.stereotype.Service
import yjh.ontongsal.api.board.domain.CommentCreatedEvent
import yjh.ontongsal.api.notification.application.input.NotificationUsecase

@Service
class NotificationEventHandler : NotificationUsecase {

    override fun alert(event: CommentCreatedEvent) {
        TODO("알림 전송 처리...")
    }
}
