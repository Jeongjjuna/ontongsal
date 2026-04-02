package yjh.ontongsal.restapi

import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.event.CommentCreatedEvent
import yjh.ontongsal.restapi.usecase.NotificationUsecase

@Service
class NotificationService : NotificationUsecase {

    override fun alert(event: CommentCreatedEvent) {
        TODO("알림 전송 처리...")
    }
}
