package yjh.ontongsal.restapi.application

import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.application.usecase.NotificationUsecase
import yjh.ontongsal.restapi.domain.event.CommentCreatedEvent

@Service
class NotificationService : NotificationUsecase {

    override fun alert(event: CommentCreatedEvent) {
        TODO("알림 전송 처리...")
    }
}
