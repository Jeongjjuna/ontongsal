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
     * 클라이언트는 /publish/{roomId} 과 같은 경로로 메세지를 발행한다.
     * 구독할 때에는 맨앞에 /topic/{roomId} 형식으로 구독한다.
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        /**
         * 메세지를 발행할 때에는 /publish 로 시작하도록 요청해야한다.
         * /publish 로 시작하는 URL 로 메세지가 발행되면, @MessageMapping 메서드로 라우팅된다.
         */
        registry.setApplicationDestinationPrefixes("/publish")
        // /topic/999 형태로 메세지를 수신(subscribe)해야한다.
        registry.enableSimpleBroker("/topic")

    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            // 클라이언트는 "/connect" 으로 연결을
            .addEndpoint("/connect")
            .setAllowedOriginPatterns("*")
            // ws:// 가 아닌 http:// 엔드포인트를 사용할 수 있게 해주는 sockJs 라이브러리를 통한 요청 허용
            .withSockJS()
    }
}
