package yjh.ontongsal.domain.lotto

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.ExpectSpec

@DisplayName("[단위 테스트] 로또 6자리 번호")
class LottoNumbersTest : ExpectSpec({

    context("로또 6자리 번호 생성할 때") {
        expect("6자리가 아니면 생성되지 않습니다.") {
            val lottoNumbers1 = listOf(
                LottoNumber(1),
                LottoNumber(2),
                LottoNumber(3),
                LottoNumber(4),
                LottoNumber(5)
            )

            shouldThrow<IllegalArgumentException> {
                LottoNumbers(lottoNumbers1)
            }

            val lottoNumbers2 = listOf(
                LottoNumber(1),
                LottoNumber(2),
                LottoNumber(3),
                LottoNumber(4),
                LottoNumber(5),
                LottoNumber(6),
                LottoNumber(7)
            )
            shouldThrow<IllegalArgumentException> {
                LottoNumbers(lottoNumbers2)
            }
        }
        expect("6자리는 모두 유니크 하지 않으면 생성되지 않습니다.") {
            val lottoNumbers = listOf(
                LottoNumber(1),
                LottoNumber(1),
                LottoNumber(3),
                LottoNumber(4),
                LottoNumber(5),
                LottoNumber(6)
            )

            shouldThrow<IllegalArgumentException> {
                LottoNumbers(lottoNumbers)
            }
        }
    }
})
