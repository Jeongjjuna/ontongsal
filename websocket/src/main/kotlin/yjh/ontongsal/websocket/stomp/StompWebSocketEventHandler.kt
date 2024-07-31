package yjh.ontongsal.websocket.stomp

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent

@Component
class StompWebSocketEventHandler {

    @EventListener
    fun handleWebSocketSessionConnectEventListener(event: SessionConnectEvent) {
        println("세션(클라이언트)가 연결을 시도합니다...")
    }

    @EventListener
    fun handleWebSocketSessionConnectedEventListener(event: SessionConnectedEvent) {
        println("세션(클라이언트)이 연결되었습니다. ${event.message}")
    }

    @EventListener
    fun handleWebSocketSessionSubscribeEventListener(event: SessionSubscribeEvent) {
        println("세션(클라이언트)이 토픽을 구독하였습니다.")
    }

    @EventListener
    fun handleWebSocketSessionUnsubscribeEventListener(event: SessionUnsubscribeEvent) {
        println("세션(클라이언트)가 구독을 취소하였습니다.")
    }

    @EventListener
    fun handleWebSocketSessionDisconnectEventListener(event: SessionDisconnectEvent) {
        println("세션(클라이언트) 연결이 종료되었습니다.")
    }
}
