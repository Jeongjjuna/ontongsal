package yjh.ontongsal.websocket.stomp

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils

/**
 * @MessageMapping : 해당 경로로 들어오는 메세지를 처리한다.
 *      registry.setApplicationDestinationPrefixes("/app") 라고 설정되어 있으므로,
 *      실제로는 클라이언트가 app/chatting-message 로 보낼 때, 처리된다.
 *
 * @SendTo : "/topic/chatting" 토픽을 구독하고 있는 모든 메서드에게 return 값을 전달한다.
 */
@Controller
class StompController {
    @MessageMapping("/chatting-message")
    @SendTo("/topic/chatting")
    fun chatting(chattingMessage: ChattingMessage): ChattingResponse {
        println("controller : 메세지를 받았습니다 >>> ${chattingMessage.message}")
        return ChattingResponse(HtmlUtils.htmlEscape(chattingMessage.message))
    }
}
