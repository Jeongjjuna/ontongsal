package event.domain

import event.domain.EventPolicy.AttendanceContext
import event.domain.EventPolicy.AttendanceEventPolicy.ConsecutiveAttendance
import event.domain.EventPolicy.AttendanceEventPolicy.DailyAttendance
import event.domain.EventPolicy.AttendanceEventPolicy.WeeklyAttendance
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@DisplayName("출석 이벤트 정책 테스트")
class AttendanceEventPolicyTest : DescribeSpec({

    describe("DailyAttendance (하루 출석 미션)") {
        val policy = DailyAttendance

        it("출석 횟수가 1회 이상이면 만족한다") {
            AttendanceContext(attendanceCount = 1).let { policy.isSatisfied(it) shouldBe true }
            AttendanceContext(attendanceCount = 5).let { policy.isSatisfied(it) shouldBe true }
        }

        it("출석 횟수가 0회이면 만족하지 않는다") {
            AttendanceContext(attendanceCount = 0).let { policy.isSatisfied(it) shouldBe false }
        }
    }

    describe("WeeklyAttendance (주간 출석 미션)") {
        val policy = WeeklyAttendance

        it("출석 횟수가 7회 이상이면 만족한다") {
            AttendanceContext(attendanceCount = 7).let { policy.isSatisfied(it) shouldBe true }
            AttendanceContext(attendanceCount = 10).let { policy.isSatisfied(it) shouldBe true }
        }

        it("출석 횟수가 7회 미만이면 만족하지 않는다") {
            AttendanceContext(attendanceCount = 6).let { policy.isSatisfied(it) shouldBe false }
        }
    }

    describe("ConsecutiveAttendance (연속 출석 미션)") {
        context("3일 연속 출석 조건일 때") {
            val policy = ConsecutiveAttendance(days = 3)

            it("연속 출석 일수가 3일 이상이면 만족한다") {
                AttendanceContext(consecutiveDays = 3).let { policy.isSatisfied(it) shouldBe true }
                AttendanceContext(consecutiveDays = 5).let { policy.isSatisfied(it) shouldBe true }
            }

            it("연속 출석 일수가 3일 미만이면 만족하지 않는다") {
                AttendanceContext(consecutiveDays = 2).let { policy.isSatisfied(it) shouldBe false }
            }
        }
    }
})
