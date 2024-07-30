package yjh.ontongsal.websocket.basic

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class BasicWebSocketHandler : TextWebSocketHandler() {

    // 모든 세션을 저장할 ConcurrentHashMap
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("클라이언트 연결 : ${session.id}")
        sessions[session.id] = session
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("클라이언트 연결 종료 : ${session.id}")
        sessions.remove(session.id)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("--------------------------------")
        println("메세지 수신 : $message")
        println("실제 데이터 : ${message.payload}")
        println("--------------------------------")

        // 연결된 모든 클라이언트에게 다시 전파하기
        broadcast(message)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println("예외 발생 :  ${session.id}, ${exception.message}")
    }

    private fun broadcast(message: TextMessage) {
        sessions.forEach { (_, session) ->
            if (session.isOpen) {
                session.sendMessage(message)
            }
        }
    } 
}
