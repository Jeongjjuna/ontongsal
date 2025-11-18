package yjh.ontongsal.websocket.stomp

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils

/**
 * @MessageMapping : 해당 경로로 들어오는 메세지를 처리한다.
 *      registry.setApplicationDestinationPrefixes("/publish") 라고 설정되어 있으므로,
 *      실제로는 클라이언트가 publish/chatting/999 로 메세지를 보내면, @MessageMapping 으로 라우팅됩니다.
 */
@Controller
class StompController(
    private val simpMessageSendingOperations: SimpMessageSendingOperations,
) {

    // 방법1
    @MessageMapping("/chatting/{roomId}") // 클라이언트는 "/publish/chatting/123" 라는 경로로 메세지를 보낸다.
    @SendTo("/topic/{roomId}") // return 값에 담긴 내용은 "/topic/123"를 구독하고 있는 모든 사람들에게 전파된다.
    fun chattingVersion1(
        @DestinationVariable roomId: Long,
        chattingMessage: ChattingMessage,
    ): ChattingResponse {
        println("controller : 메세지를 받았습니다 >>> ${chattingMessage.message}")
        return ChattingResponse(HtmlUtils.htmlEscape(chattingMessage.message))
    }

    // 방법2
    @MessageMapping("/chatting/{roomId}")
    fun chattingVersion2(
        @DestinationVariable roomId: Long,
        chattingMessage: ChattingMessage,
    ) {
        println("controller : 메세지를 받았습니다 >>> ${chattingMessage.message}")

        simpMessageSendingOperations.convertAndSend(
            "/topic/$roomId",
            ChattingResponse(HtmlUtils.htmlEscape(chattingMessage.message))
        )
    }
}
