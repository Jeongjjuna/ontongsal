package event.domain

import event.domain.EventPolicy.MissionContext
import event.domain.EventPolicy.MissionEventPolicy.ManualParticipation
import event.domain.EventPolicy.MissionEventPolicy.PreRegistrationCompleted
import event.domain.EventPolicy.MissionEventPolicy.ReachGameLevel
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@DisplayName("미션 이벤트 정책 테스트")
class MissionEventPolicyTest : DescribeSpec({

    describe("ReachGameLevel (게임 레벨 도달 미션)") {
        context("목표 레벨이 10일 때") {
            val policy = EventPolicy.MissionEventPolicy.ReachGameLevel(level = 10)

            it("현재 레벨이 10이면 만족한다") {
                val context = MissionContext(userLevel = 10)
                policy.isSatisfied(context) shouldBe true
            }

            it("현재 레벨이 11이면 만족한다") {
                val context = MissionContext(userLevel = 11)
                policy.isSatisfied(context) shouldBe true
            }

            it("현재 레벨이 9이면 만족하지 않는다") {
                val context = MissionContext(userLevel = 9)
                policy.isSatisfied(context) shouldBe false
            }

            it("레벨 정보가 없으면 만족하지 않는다") {
                val context = MissionContext(userLevel = null)
                policy.isSatisfied(context) shouldBe false
            }
        }
    }

    describe("PreRegistrationCompleted (사전 예약 완료 미션)") {
        val policy = PreRegistrationCompleted

        it("사전 예약을 완료했으면 만족한다") {
            val context = MissionContext(hasPreRegistered = true)
            policy.isSatisfied(context) shouldBe true
        }

        it("사전 예약을 완료하지 않았으면 만족하지 않는다") {
            val context = MissionContext(hasPreRegistered = false)
            policy.isSatisfied(context) shouldBe false
        }
    }

    describe("ManualParticipation (수동 참여 미션)") {
        val policy = ManualParticipation

        it("항상 만족한다") {
            val context = MissionContext()
            policy.isSatisfied(context) shouldBe true
        }
    }
})
