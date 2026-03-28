package yjh.ontongsal.websocket.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@EnableWebSocket
@Configuration
class BasicWebSocketConfig(
    private val basicWebSocketHandler: BasicWebSocketHandler,
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        /**
         * "/connect" url 로 websocket 연결 요청이 들어오면, 핸들러 클래스가 처리한다.
         * 이 요청은 HTTP 요청은 아니고, ws://localhost:8080/connect 요청이다.
         */
        registry.addHandler(basicWebSocketHandler, "/connect")
            .setAllowedOrigins("*")
    }
}
