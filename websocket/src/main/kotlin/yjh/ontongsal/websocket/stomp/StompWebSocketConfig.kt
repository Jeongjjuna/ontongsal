package yjh.ontongsal.websocket.stomp

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@EnableWebSocketMessageBroker
@Configuration
class StompWebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic") // 브로커는 해당 경로를 받아들인다.
        registry.setApplicationDestinationPrefixes("/app") // @MessageMapping 경로의 맨 앞에 붙는다. 메세지를 받을 때 경로의 접두어
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/stomp") // 클라이언트가 웹소켓 연결을 시도할 url
            .setAllowedOriginPatterns("*")
    }
}
