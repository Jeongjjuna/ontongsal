package yjh.ontongsal.testing.config

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import yjh.ontongsal.testing.config.KafkaTestContainer.KAFKA_CONTAINER
import yjh.ontongsal.testing.config.MySQLTestContainer.MYSQL_CONTAINER
import yjh.ontongsal.testing.config.RedisTestContainer.REDIS_CONTAINER

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class IntegrationTest {

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun setDataSourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl)
            registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername)
            registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword)
        }

        @JvmStatic
        @DynamicPropertySource
        fun setRedisProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost)
            registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort)
        }

        @JvmStatic
        @DynamicPropertySource
        fun setKafkaProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        }
    }
}
