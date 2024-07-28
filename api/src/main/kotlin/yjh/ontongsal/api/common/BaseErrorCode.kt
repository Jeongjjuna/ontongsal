package yjh.ontongsal.api.common

import org.springframework.http.HttpStatus

enum class BaseErrorCode(
    val httpStatus: HttpStatus,
    val code: Int,
    val message: String,
) {
    // 200
    SUCCESS(HttpStatus.OK, 2000, "요청 성공"),

    // 404
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, 4041, "게시글을 찾을 수 없습니다"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "서버 내부 에러"),
}
