package yjh.ontongsal.websocket.stomp.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import yjh.ontongsal.websocket.stomp.ChatMessageSubscriber

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,

    @Value("\${spring.data.redis.port}")
    private val port: Int,
) {

    @Bean
    fun chatPubSubConnectionFactory(): RedisConnectionFactory {
        val configuration = RedisStandaloneConfiguration(host, port)
        // redis pub/sub 에서는 특정 데이터베이스에 의존적이지 않으므로, database 를 지정할 필요가 없다.
        // configuration.database = 0

        return LettuceConnectionFactory(configuration);
    }

    @Bean("chatPublishRedisTemplate")
    fun springRedisTemplate(
        @Qualifier("chatPubSubConnectionFactory") chatPubSubConnectionFactory: RedisConnectionFactory,
    ): StringRedisTemplate {
        // 일반적으로 RedisTemplate<key 데이터타입, value 데이터타입>을 사용하지만, 여기서는 간단하게 StringRedisTemplate 사용
        return StringRedisTemplate(chatPubSubConnectionFactory)
    }

    @Bean("chatSubscribeMessageListenerContainer")
    fun redisMessageListenerContainer(
        @Qualifier("chatPubSubConnectionFactory") chatPubSubConnectionFactory: RedisConnectionFactory,
        messageListenerAdapter: MessageListenerAdapter,
    ): RedisMessageListenerContainer {

        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(chatPubSubConnectionFactory)
        container.addMessageListener(messageListenerAdapter, PatternTopic("chat"))
        return container
    }

    @Bean
    fun messageListenerAdapter(chatMessageSubscriber: ChatMessageSubscriber): MessageListenerAdapter {
        // RedisPubSubService 의 특정 메서드가 수신된 메시지를 처리할수 있도록 지정
        return MessageListenerAdapter(chatMessageSubscriber, "onMessage");

    }

}
