package yjh.ontongsal.domain.lotto

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import yjh.ontongsal.domain.lotto.command.LottoCreateCommand
import yjh.ontongsal.domain.lotto.command.PrizePolicyCommand

@DisplayName("[단위 테스트] 로또 회차")
class LottoTest : ExpectSpec({

    val startAt = Instant.ofEpochSecond(1700000000)
    val endAt = Instant.ofEpochSecond(1700003600)    // startAt + 3600초 (1시간 후)
    val validLottoCreateCommand = LottoCreateCommand(
        id = 1,
        round = 1,
        isActive = true,
        startAt = startAt,
        endAt = endAt,
        createdBy = "ADMIN",
        prizePolicyCommand = PrizePolicyCommand(
            firstPercent = 70,
            secondPercent = 20,
            thirdPercent = 5,
            fourthPercent = 3,
            fifthPercent = 2
        )
    )

    context("로또 회차 생성") {

        expect("회차 번호는 1 이상 이어야 한다.") {
            shouldNotThrowAny {
                Lotto.of(validLottoCreateCommand.copy(round = 1))
            }
            shouldThrow<IllegalArgumentException> {
                Lotto.of(validLottoCreateCommand.copy(round = 0))
            }
        }

        expect("startAt < endAt 이어야 한다.") {
            shouldThrow<IllegalArgumentException> {
                val now = Instant.now()
                Lotto.of(
                    validLottoCreateCommand.copy(
                        startAt = now.plusSeconds(3600),
                        endAt = now
                    )
                )
            }
            shouldThrow<IllegalArgumentException> {
                val now = Instant.now()
                Lotto.of(
                    validLottoCreateCommand.copy(
                        startAt = now,
                        endAt = now
                    )
                )
            }
        }
    }

    context("로또 회차 응모가능 여부") {

        val activeLotto = Lotto.of(validLottoCreateCommand.copy(isActive = true))
        expect("활성화 상태일 때 응모 가능 하다.") {
            activeLotto.canEntry(startAt.plusSeconds(1)) shouldBe true
        }

        val notActiveLotto = Lotto.of(validLottoCreateCommand.copy(isActive = false))
        expect("비활성화 상태로는 응모할 수 없다.") {
            notActiveLotto.canEntry(startAt.plusSeconds(1)) shouldBe false
        }

        val beforeStart = startAt.minusSeconds(1)
        val afterEnd = endAt.plusSeconds(1)
        expect("startAt 이전에는 응모할 수 없다.") {
            activeLotto.canEntry(beforeStart) shouldBe false
        }

        expect("endAt 이후에는 응모할 수 없다.") {
            activeLotto.canEntry(afterEnd) shouldBe false
        }
    }

    context("로또 회차 추첨가능 여부") {

        expect("회차가 아직 시작하지 않았다면 추첨 불가") {
            val lotto = Lotto.of(validLottoCreateCommand)
            lotto.canDraw(startAt.minusSeconds(1)) shouldBe false
        }

        expect("회차가 아직 종료되지 않았다면 추첨 불가") {
            val lotto = Lotto.of(validLottoCreateCommand)
            lotto.canDraw(endAt.minusSeconds(1)) shouldBe false
        }

        expect("이미 당첨번호가 존재하면 추첨 불가") {
            val lotto = Lotto.of(validLottoCreateCommand)
            lotto.draw(
                LottoNumbers(listOf(1, 2, 3, 4, 5, 6).map { LottoNumber(it) }),
                LottoNumber(7),
                "ADMIN"
            )
            lotto.canDraw(endAt.plusSeconds(1)) shouldBe false
        }

        expect("회차가 종료되고 당첨번호가 없다면 추첨 가능") {
            val lotto = Lotto.of(validLottoCreateCommand)
            lotto.canDraw(endAt.plusSeconds(1)) shouldBe true
        }
    }
})
