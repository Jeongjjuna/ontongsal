package functional

import arrow.core.Either
import arrow.core.left
import arrow.core.right

/*
 * 함수형 도메인 모델링 기초 정리
 *
 *   곱타입(AND) = data class               : 모든 필드를 "동시에" 가짐
 *   합타입(OR)  = sealed interface / enum   : 여러 경우 중 "하나"만 가짐
 *   값타입(VO)  = value class               : 원시값을 의미 있는 타입으로 감쌈
 *   에러 처리   = Either<실패, 성공>
 */

// ───────────────────────────────────────────────
// 곱타입 (AND type) - 모든 값을 함께 가진다
// ───────────────────────────────────────────────
data class FruitSalad(
    val apple: AppleVariety,
    val banana: BananaVariety,
    val cherries: CherryVariety,
)

// ───────────────────────────────────────────────
// 합타입 (OR type) - 예시 1 : 여러 경우 중 하나
// ───────────────────────────────────────────────
sealed interface FruitSnack
enum class AppleVariety : FruitSnack { GoldenDelicious, GrannySmith, Fuji }
enum class BananaVariety : FruitSnack { Cavendish, GrosMichel, Manzano }
enum class CherryVariety : FruitSnack { Montmorency, Bing }

// ───────────────────────────────────────────────
// 합타입 (OR type) - 예시 2 : 결제 수단
// ───────────────────────────────────────────────
enum class CardType { Visa, Mastercard }

@JvmInline
value class CardNumber(val value: Int)

data class CreditCardInfo(
    val cardType: CardType,
    val cardNumber: CardNumber,
)

sealed interface PaymentMethod {
    data object Cash : PaymentMethod

    @JvmInline
    value class CheckNumber(val value: Double) : PaymentMethod

    data class CreditCardInfo(
        val cardType: CardType,
        val cardNumber: CardNumber,
    ) : PaymentMethod
}

// ───────────────────────────────────────────────
// 값 타입 (VO)
// ───────────────────────────────────────────────
@JvmInline
value class ProductCode(val value: String)

// 합타입 안에서 값 타입을 만드는 패턴
sealed interface OrderQuantity {
    @JvmInline
    value class Unit(val value: Int) : OrderQuantity

    @JvmInline
    value class Kilogram(val value: Double) : OrderQuantity
}

val anOrderQtyInUnits = OrderQuantity.Unit(10)

// ───────────────────────────────────────────────
// Payment 도메인
// ───────────────────────────────────────────────
@JvmInline
value class PaymentAmount(val value: Double)

enum class Currency { EUR, USD }

class Payment(
    val amount: PaymentAmount,
    val currency: Currency,
    val method: PaymentMethod,
)

// 상태가 없는 에러는 data object 로 (원래 class ... { ... } 자리)
sealed interface PaymentError
data object CardTypeNotRecognized : PaymentError
data object PaymentRejected : PaymentError
data object PaymentProviderOffline : PaymentError

// 예시용 플레이스홀더 타입 (실제 도메인에서는 따로 정의)
class UnpaidInvoice
class PaidInvoice

// 함수 시그니처를 타입으로 표현
typealias PayInvoice = (UnpaidInvoice, Payment) -> Either<PaymentError, PaidInvoice>
typealias ConvertPaymentCurrency = (Payment, Currency) -> Payment

// ───────────────────────────────────────────────
// 주소 검증
// ───────────────────────────────────────────────
sealed interface AddressValidationError
data object InvalidFormat : AddressValidationError
data object AddressNotFound : AddressValidationError

class UnvalidatedAddress
class CheckedAddress

// 수신 객체(receiver)를 쓰는 시그니처
typealias CheckAddressExists =
        UnvalidatedAddress.() -> Either<AddressValidationError, CheckedAddress>

// ───────────────────────────────────────────────
// Either 사용 예시
// ───────────────────────────────────────────────

// 성공 반환
fun test1(): Either<AddressValidationError, String> {
    return "bbb".right()                 // = Either.Right("bbb")
}

// 실패 반환
fun test2(): Either<AddressValidationError, String> {
    return AddressNotFound.left()        // = Either.Left(AddressNotFound)
}

fun main() {
    // 1) 성공/실패만 구분
    when (val result = test1()) {
        is Either.Left  -> println("실패: ${result.value}")
        is Either.Right -> println("성공: ${result.value}")
    }

    // 2) 실패를 다시 종류별로 분기 (when 이 exhaustive 하게 강제됨)
    when (val result = test2()) {
        is Either.Left -> when (result.value) {
            InvalidFormat   -> println("포맷 오류")
            AddressNotFound -> println("주소 못 찾음")
        }
        is Either.Right -> println("성공: ${result.value}")
    }
}