package yjh.ontongsal.api.common

import kotlin.RuntimeException

class BaseException(
    val baseErrorCode: BaseErrorCode,
) : RuntimeException(baseErrorCode.message)

fun validate(value: Boolean, lazyErrorCode: () -> BaseErrorCode) {
    if (!value) throw BaseException(lazyErrorCode())
}
