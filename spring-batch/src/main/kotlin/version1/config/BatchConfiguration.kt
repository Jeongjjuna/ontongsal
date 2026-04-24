package wemade.ontongsal.version1.config

import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.configuration.support.MapJobRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * DefaultBatchConfiguration : spring batch 기본 설정 클래스
 * - 배치 실행에 필요한 핵심 컴포넌트 구성해주는 역할
 */
@Configuration
class BatchConfiguration : DefaultBatchConfiguration() {

    /**
     * CommandLineJobOperator : 진입점 클래로 커맨드라인에서 Job 이름을 입력받아 실제 Job을 실행
     * - Job 이름으로 실행할 Job 인스턴스가 필요한데, 그게 JobRegistry 이다.
     * - 즉, CommandLineJobOperator 를 사용하기 위해 JobRegistry 빈이 필요하다.
     */
    @Bean
    fun jobRegistry(): JobRegistry {
        return MapJobRegistry()
    }
}
