package yjh.ontongsal.testing.common.circuitbreaker

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import java.time.Duration
import kotlin.test.Test

class BusinessException : RuntimeException()
class ExternalCallException : RuntimeException()

class CircuitBreakerAdapterTest {

    private lateinit var registry: CircuitBreakerRegistry
    private lateinit var sut: CircuitBreakerAdapter

    private fun testRegistry(): CircuitBreakerRegistry {
        val testConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50f)
            .slidingWindowSize(4)
            .minimumNumberOfCalls(4)
            .waitDurationInOpenState(Duration.ofMillis(500))
            .permittedNumberOfCallsInHalfOpenState(2)
            // 외부 장애는 실패로 기록
            .recordExceptions(
                ExternalCallException::class.java
            )
            // 비즈니스 예외는 무시
            .ignoreExceptions(
                BusinessException::class.java
            )
            .build()

        val registry = CircuitBreakerRegistry.ofDefaults()
        registry.addConfiguration("test", testConfig) // config 이름
        registry.circuitBreaker("test", "test") // 서킷브레이커 이름, config 이름
        return registry
    }

    // 서킷브레이커를 OPEN 상태로 만드는 헬퍼
    // slidingWindowSize=4, failureRateThreshold=50% → 4번 중 2번 이상 실패 시 OPEN
    private fun openCircuit() {
        repeat(2) { callWithSuccess() }
        repeat(2) { callWithFailure() }
    }

    private fun callWithSuccess() {
        sut.run("test") {
            "ok"
        }
    }

    private fun callWithFailure() {
        runCatching {
            sut.run("test") {
                throw ExternalCallException()
            }
        }
    }

    @BeforeEach
    fun setUp() {
        registry = testRegistry()
        sut = CircuitBreakerAdapter(registry)
    }

    @Test
    fun `실패율이 임계치 미만이면 CLOSED 상태를 유지한다`() {
        // given
        // 4번 호출 중 1번(25%) 실패 → 임계치(50%) 미만 → CLOSED 유지
        repeat(3) { callWithSuccess() }
        repeat(1) { callWithFailure() }

        // when
        val state = registry.circuitBreaker("test").state

        // then
        assertThat(state).isEqualTo(CircuitBreaker.State.CLOSED)
    }

    @Test
    fun `기록 대상 예외가 발생하면 실패로 집계된다`() {
        // given
        // minimumNumberOfCalls=4 를 채우되 ExternalCallException 4번 → 실패율 100%
        repeat(4) { callWithFailure() }

        // when
        val state = registry.circuitBreaker("test").state

        // then
        assertThat(state).isEqualTo(CircuitBreaker.State.OPEN)
    }

    @Test
    fun `장애 발생 후 대기 시간이 경과하고 호출이 성공하면 CLOSED 상태로 자동 복구된다`() {
        // 1단계: 초기 상태는 CLOSED
        assertThat(registry.circuitBreaker("test").state)
            .isEqualTo(CircuitBreaker.State.CLOSED)

        // 2단계: 실패율 초과 → OPEN
        openCircuit()
        assertThat(registry.circuitBreaker("test").state)
            .isEqualTo(CircuitBreaker.State.OPEN)

        // 3단계: OPEN 상태에서는 호출 자체를 막음
        assertThatThrownBy { sut.run("test") { "blocked" } }
            .isInstanceOf(CallNotPermittedException::class.java)

        // 4단계: waitDuration 경과 후 HALF_OPEN으로 전환
        Thread.sleep(600)
        runCatching { sut.run("test") { "probe" } }
        assertThat(registry.circuitBreaker("test").state)
            .isEqualTo(CircuitBreaker.State.HALF_OPEN)

        // 5단계: HALF_OPEN에서 성공 → CLOSED 복구
        repeat(2) { sut.run("test") { "ok" } }
        assertThat(registry.circuitBreaker("test").state)
            .isEqualTo(CircuitBreaker.State.CLOSED)
    }

    @Test
    fun `정상 호출이면 결과를 반환한다`() {
        // given
        val expected = "hello"

        // when
        val result = sut.run("test") { expected }

        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `실패율이 임계치에 도달하면 OPEN 상태로 전환된다`() {
        // given
        // minimumNumberOfCalls=4 를 채워야 실패율 계산 시작
        // 4번 호출 중 2번(50%) 실패 → 임계치(50%) 도달로 OPEN
        openCircuit()

        // when
        val state = registry.circuitBreaker("test").state

        // then waitDurationInOpenState
        assertThat(state).isEqualTo(CircuitBreaker.State.OPEN)
    }

    @Test
    fun `OPEN 상태이면 호출을 차단하고 예외를 던진다`() {
        // given
        openCircuit()
        var operationExecuted = false

        // when & then
        assertThatThrownBy {
            sut.run("test") {
                operationExecuted = true
            }
        }.isInstanceOf(CallNotPermittedException::class.java)

        assertThat(operationExecuted).isFalse()
    }

    @Test
    fun `무시 대상 예외가 발생하면 실패로 집계되지 않아 CLOSED 상태를 유지한다`() {
        // given
        // 4번 모두 BusinessException → 실패로 기록되지 않으므로 실패율 0%
        repeat(4) {
            runCatching { sut.run("test") { throw BusinessException() } }
        }

        // when
        val state = registry.circuitBreaker("test").state

        // then
        assertThat(state).isEqualTo(CircuitBreaker.State.CLOSED)
    }

    @Test
    fun `OPEN 상태에서 대기 시간이 경과하면 HALF_OPEN 상태로 전환된다`() {
        // given
        openCircuit()

        // when
        Thread.sleep(600) // waitDurationInOpenState(500ms) 대기

        // then
        // HALF_OPEN은 실제 호출 시도 시점에 전환되므로 probe 호출
        runCatching { sut.run("test") { "probe" } }
        assertThat(registry.circuitBreaker("test").state)
            .isEqualTo(CircuitBreaker.State.HALF_OPEN)
    }

    @Test
    fun `OPEN 상태에서 대기 시간이 경과하지 않으면 OPEN 상태를 유지한다`() {
        // given
        openCircuit()

        // when
        Thread.sleep(100) // waitDurationInOpenState(500ms) 미경과

        // then
        assertThat(registry.circuitBreaker("test").state)
            .isEqualTo(CircuitBreaker.State.OPEN)
    }

    @Test
    fun `HALF_OPEN 상태에서 허용된 호출이 모두 성공하면 CLOSED 상태로 복구된다`() {
        // given
        openCircuit()
        Thread.sleep(600)

        // when
        // permittedNumberOfCallsInHalfOpenState=2 → 2번 성공 시 CLOSED
        repeat(2) { sut.run("test") { "ok" } }

        // then
        assertThat(registry.circuitBreaker("test").state)
            .isEqualTo(CircuitBreaker.State.CLOSED)
    }

    @Test
    fun `HALF_OPEN 상태에서 실패가 발생하면 OPEN 상태로 전환된다`() {
        // given
        openCircuit()
        Thread.sleep(600)

        // when
        // permittedNumberOfCallsInHalfOpenState=2 → 1번이라도 실패 시 OPEN 복귀
        sut.run("test") { "ok" }
        runCatching {
            sut.run("test") { throw ExternalCallException() }
        }

        // then
        assertThat(registry.circuitBreaker("test").state)
            .isEqualTo(CircuitBreaker.State.OPEN)
    }

    @Test
    fun `최소 호출 수에 미달하면 실패율과 무관하게 CLOSED 상태를 유지한다`() {
        // given
        // minimumNumberOfCalls=4 미달 (3번만 호출)
        repeat(3) { callWithFailure() }

        // when
        val state = registry.circuitBreaker("test").state

        // then
        assertThat(state).isEqualTo(CircuitBreaker.State.CLOSED)
    }

}
