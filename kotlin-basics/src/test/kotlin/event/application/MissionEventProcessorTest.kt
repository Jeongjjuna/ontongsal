package event.application

import event.application.port.PreRegistrationClient
import event.application.port.UserLevelReader
import event.domain.EventPolicy.MissionEventPolicy.ManualParticipation
import event.domain.EventPolicy.MissionEventPolicy.PreRegistrationCompleted
import event.domain.EventPolicy.MissionEventPolicy.ReachGameLevel
import event.domain.EventResult
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

private class FakeUserLevelReader(
    private val level: Int = 0,
) : UserLevelReader {

    override fun getUserLevel(userId: String): Int {
        return level
    }
}

private class FakePreRegistrationClient(
    private val completed: Boolean = false,
) : PreRegistrationClient {

    override fun hasCompleted(userId: String): Boolean {
        return completed
    }
}

class MissionEventProcessorTest : DescribeSpec({
    val userId = "user-1"

    describe("게임 레벨 도달 미션 (ReachGameLevel)") {
        fun createProcessor(level: Int) = MissionEventProcessor(
            userLevelReader = FakeUserLevelReader(level = level),
            preRegistrationClient = FakePreRegistrationClient()
        )

        context("사용자 레벨이 목표 레벨 이상이면") {
            it("성공한다") {
                val processor = createProcessor(level = 10)

                val result = processor.process(userId, ReachGameLevel(level = 10))

                result.shouldBeInstanceOf<EventResult.Success>()
            }
        }

        context("사용자 레벨이 목표 레벨보다 낮으면") {
            it("실패한다") {
                val processor = createProcessor(level = 9)

                val result = processor.process(userId, ReachGameLevel(level = 10))

                result shouldBe EventResult.Failure.ConditionNotMet
            }
        }
    }

    describe("사전 예약 완료 미션 (PreRegistrationCompleted)") {
        fun createProcessor(completed: Boolean) = MissionEventProcessor(
            userLevelReader = FakeUserLevelReader(),
            preRegistrationClient = FakePreRegistrationClient(completed = completed)
        )

        context("사전 예약을 완료했으면") {
            it("성공한다") {
                val processor = createProcessor(completed = true)

                val result = processor.process(userId, PreRegistrationCompleted)

                result.shouldBeInstanceOf<EventResult.Success>()
            }
        }

        context("사전 예약을 완료하지 않았으면") {
            it("실패한다") {
                val processor = createProcessor(completed = false)

                val result = processor.process(userId, PreRegistrationCompleted)

                result shouldBe EventResult.Failure.ConditionNotMet
            }
        }
    }

    describe("수동 참여 미션 (ManualParticipation)") {
        val processor = MissionEventProcessor(
            userLevelReader = FakeUserLevelReader(),
            preRegistrationClient = FakePreRegistrationClient()
        )

        context("수동 참여 요청 시") {
            it("성공한다") {
                val result = processor.process(userId, ManualParticipation)

                result.shouldBeInstanceOf<EventResult.Success>()
            }
        }
    }
})