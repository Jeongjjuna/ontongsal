package yjh.ontongsal.api.notification.adapter.input.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import yjh.ontongsal.api.board.domain.CommentCreatedEvent
import yjh.ontongsal.api.notification.application.input.NotificationUsecase

@Component
class NotificationConsumer(
    private val notificationUsecase: NotificationUsecase,
) {
    private val logger = KotlinLogging.logger {}

    @EventListener
    @Async("eventConsumeTaskExecutor")
    fun handleCommentCreated(event: CommentCreatedEvent) {
        logger.info { "Event received : $event" }
        notificationUsecase.alert(event)
        logger.info { "Event completed successfully : $event " }
    }
}
