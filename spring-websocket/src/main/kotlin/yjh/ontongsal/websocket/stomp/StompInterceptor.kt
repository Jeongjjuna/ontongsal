package yjh.ontongsal.websocket.stomp

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class StompInterceptor: ChannelInterceptor {

    override fun preSend(
        message: Message<*>,
        channel: MessageChannel,
    ): Message<*>? {
        // 인증 로직(토큰 검증 로직 작성)
        val accessor: StompHeaderAccessor = StompHeaderAccessor.wrap(message)

        if(StompCommand.CONNECT == accessor.command) {
            // connect 요청일때 토큰 유효성을 검사한다.
            val bearerToken = accessor.getFirstNativeHeader("Authorization")
            val token = bearerToken
                ?.takeIf { it.startsWith("Bearer ") }
                ?.substring(7)
                ?: throw IllegalArgumentException("Missing or invalid Authorization header")

            // 토큰 검증
        }

        return message
    }
}
