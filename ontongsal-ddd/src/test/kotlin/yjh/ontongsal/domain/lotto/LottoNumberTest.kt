package yjh.ontongsal.domain.lotto

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.ExpectSpec

@DisplayName("[단위 테스트] 로또 번호")
class LottoNumberTest : ExpectSpec({

    context("로또 번호 생성할 때") {
        expect("1 ~ 45 사이의 번호만 생성 된다.") {
            shouldNotThrowAny {
                LottoNumber(1)
                LottoNumber(25)
                LottoNumber(45)
            }
        }
        expect("범위를 벗어나는 숫자는 생성되지 않는다.") {
            shouldThrow<IllegalArgumentException> {
                LottoNumber(0)
            }
            shouldThrow<IllegalArgumentException> {
                LottoNumber(46)
            }
        }
    }
})
