package yjh.ontongsal.websocket.stomp

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@EnableWebSocketMessageBroker
@Configuration
class StompWebSocketConfig : WebSocketMessageBrokerConfigurer {

    /**
     * 클라이언트는 /topic/{roomId} 과 같은 경로로 구독할 수 있다.
     * 구독할 때에는 맨앞에 /topic을 반드시 붙여야 한다.
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app") // @MessageMapping 경로의 맨 앞에 붙는다. 메세지를 받을 때 경로의 접두어
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/connect/websocket") // 클라이언트는 "/connect/websocket" 으로 연결을 시도할 수 있다.
            .setAllowedOriginPatterns("*")
    }
}
