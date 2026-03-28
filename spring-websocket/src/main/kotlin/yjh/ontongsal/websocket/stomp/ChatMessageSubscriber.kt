package yjh.ontongsal.websocket.stomp

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component
import org.springframework.web.util.HtmlUtils

/**
 * redis subscribe 하는 객체이다.
 */
@Component
class ChatMessageSubscriber(
    private val messageTemplate: SimpMessageSendingOperations,
) : MessageListener {

    // pattern 에는 topic 의 이름의 패턴이 담겨있고, 이 패턴을 기반으로 다이나믹한 코딩
    override fun onMessage(message: Message, pattern: ByteArray?) {
        val payload: String = String(message.body);
        val objectMapper = ObjectMapper()

        // 1 메시지 파싱
        val pubSubDTO: ChattingPubSubDTO = try {
            objectMapper.readValue(payload, ChattingPubSubDTO::class.java)
        } catch (e: JsonProcessingException) {
            println("Failed to parse Redis message: $payload, error=${e.message}")
            return
        }

        //  2. 메시지 전송
        try {
            messageTemplate.convertAndSend(
                "/topic/${pubSubDTO.roomId}",
                ChattingResponse(HtmlUtils.htmlEscape(pubSubDTO.message))
            )
        } catch (e: Exception) {
            println("Failed to send WebSocket message for roomId=${pubSubDTO.roomId}, error=${e.message}")
        }
    }
}
