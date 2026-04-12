package yjh.ontongsal.restapi.adapter.web

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.usecase.NotificationUsecase
import yjh.ontongsal.restapi.domain.event.CommentCreatedEvent

private val logger = KotlinLogging.logger {}

@Component
class NotificationEventListener(
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
