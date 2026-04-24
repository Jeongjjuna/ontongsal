package wemade.ontongsal.version1.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.infrastructure.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * job 실행하는법
 * ./gradlew run --args="wemade.ontongsal.version1.config.MusicPlaybackJobConfiguration start musicPlaybackJob"
 */
@Configuration
@Import(BatchConfiguration::class) // BatchConfiguration의 모든 빈 설정을 가져온다. 덕분에 JobRepository 같은 배치 핵심 컴포넌트들을 즉시 주입받아 사용할 수 있다.
@EnableBatchProcessing
class MusicPlaybackJobConfiguration(
    private val jobRepository: JobRepository, // JobRepository는 DefaultBatchConfiguration이 자동으로 구성해주는 핵심 컴포넌트다.
) {
    private var tracksPlayed = 0
    private val playlistSize = 5

    /**
     * MapJobRegistry 가 Job 타입의 구현체들을 수집해서 빈으로 등록해준다.
     */
    @Bean
    fun musicPlaybackJob(
        loadPlaylistStep: Step,
        tuneInstrumentsStep: Step,
        playTrackStep: Step,
        encoreStep: Step,
    ): Job {
        return JobBuilder(jobRepository) // Job의 실행 이력을 저장하고 관리할 때 사용된다
            .start(loadPlaylistStep)
            .next(tuneInstrumentsStep)
            .next(playTrackStep)
            .next(encoreStep)
            .build()
    }

    @Bean
    fun loadPlaylistStep(): Step {
        return StepBuilder(jobRepository)
            .tasklet { contribution, chunkContext ->
                println("플레이리스트를 불러옵니다...")
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun tuneInstrumentsStep(): Step {
        return StepBuilder(jobRepository)
            .tasklet { _, _ ->
                println("악기 튜닝 중... A=440Hz")
                println("오늘의 세트리스트: 총 ${playlistSize}곡")
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun playTrackStep(): Step {
        return StepBuilder(jobRepository)
            .tasklet { _, _ ->
                val current = ++tracksPlayed
                println("${current}번 트랙 재생 완료 (${current}/$playlistSize)")

                if (current < playlistSize) {
                    return@tasklet RepeatStatus.CONTINUABLE
                }
                return@tasklet RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun encoreStep(): Step {
        return StepBuilder(jobRepository)
            .tasklet { _, _ ->
                println("앵콜! 공연이 끝났습니다. 총 ${playlistSize}곡 재생 완료")
                RepeatStatus.FINISHED
            }
            .build()
    }
}
