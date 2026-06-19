package event.domain

import event.domain.EventPolicy.InvitationContext
import event.domain.EventPolicy.InvitationEventPolicy.JoinWithInvitationCode
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@DisplayName("초대 이벤트 정책 테스트")
class InvitationEventPolicyTest : DescribeSpec({

    describe("JoinWithInvitationCode (초대 코드 참여 미션)") {
        val policy = JoinWithInvitationCode

        it("초대 코드를 사용했으면 만족한다") {
            val context = InvitationContext(hasUsedInviteCode = true)
            policy.isSatisfied(context) shouldBe true
        }

        it("초대 코드를 사용하지 않았으면 만족하지 않는다") {
            val context = InvitationContext(hasUsedInviteCode = false)
            policy.isSatisfied(context) shouldBe false
        }
    }
})
