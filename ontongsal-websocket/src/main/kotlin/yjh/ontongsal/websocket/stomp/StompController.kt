package yjh.ontongsal.websocket.stomp

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils

/**
 * @MessageMapping : 해당 경로로 들어오는 메세지를 처리한다.
 *      registry.setApplicationDestinationPrefixes("/app") 라고 설정되어 있으므로,
 *      실제로는 클라이언트가 app/chatting-message 로 보낼 때, 처리된다.
 */
@Controller
class StompController {

    /**
     * 클라이언트 -> chatting/roomId로 메세지 전송 -> 서버는 해당 메세지를 /topic/roomId를 구독한 사람들에게 전파
     */
    @MessageMapping("/chatting/{roomId}") // 클라이언트는 "/app/chat/123" 라는 경로로 메세지를 보낸다.
    @SendTo("/topic/rooms/{roomId}") // return 값에 담긴 내용은 "/topic/123"를 구독하고 있는 모든 사람들에게 전파된다.
    fun chatting(
        @DestinationVariable roomId: Long,
        chattingMessage: ChattingMessage
    ): ChattingResponse {
        println("controller : 메세지를 받았습니다 >>> ${chattingMessage.message}")
        return ChattingResponse(HtmlUtils.htmlEscape(chattingMessage.message))
    }
}
