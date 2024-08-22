package yjh.ontongsal.websocket.stomp.broadcast

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import yjh.ontongsal.websocket.stomp.ChattingResponse

@RestController
class BroadCastController(
    private val messagingTemplate: SimpMessagingTemplate
) {

    @PostMapping("/broadcast")
    fun broadcast() {
        println("broadcast 요청")
        val roomId = 777
        val destination = "/topic/rooms/${roomId}" // 특정 사용자 경로
        messagingTemplate.convertAndSend(destination, ChattingResponse("구독자들에게 전파하기!"))
    }
}