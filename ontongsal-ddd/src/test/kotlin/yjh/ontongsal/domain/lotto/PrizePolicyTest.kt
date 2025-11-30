package yjh.ontongsal.domain.lotto

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.ExpectSpec
import yjh.ontongsal.domain.lotto.command.PrizePolicyCommand

@DisplayName("[단위 테스트] 로또 추첨 정책")
class PrizePolicyTest : ExpectSpec({

    context("로또 추첨 정책 생성") {
        val validPrizePolicy = PrizePolicyCommand(70, 20, 5, 3, 2)

        expect("상금 비율의 총합은 100% 여야 합니다.") {
            shouldNotThrowAny {
                PrizePolicy.of(validPrizePolicy)
            }
        }
        expect("상금 비율 총합이 100%가 아니면 생성되지 않습니다.") {
            shouldThrow<IllegalArgumentException> {
                PrizePolicy.of(validPrizePolicy.copy(secondPercent = 100))
            }
        }
        expect("상금 비율은 음수가 될 수 없습니다.") {
            shouldThrow<IllegalArgumentException> {
                PrizePolicy.of(validPrizePolicy.copy(fourthPercent = 9, firstPercent = -4))
            }
        }
    }
})
