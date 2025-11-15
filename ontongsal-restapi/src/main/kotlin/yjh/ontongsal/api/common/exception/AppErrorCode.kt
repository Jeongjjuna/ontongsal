package yjh.ontongsal.api.common.exception


enum class ErrorCode(
    val code: Int,
    val message: String,
) {
    // User (1000~1999)
    USER_NOT_FOUND(1000, "사용자를 찾을 수 없습니다"),
    USER_CONFLICT(1001, "사용자 충돌 발생"),

    // Post (2000~2999)
    POST_NOT_FOUND(2000, "게시글을 찾을 수 없습니다"),
    POST_CONFLICT(2001, "게시글 충돌 발생"),

    // Comment (3000~3999)
    COMMENT_NOT_FOUND(3000, "댓글을 찾을 수 없습니다"),
    COMMENT_CONFLICT(3001, "댓글 충돌 발생"),
    COMMENT_DELETED(3002, "삭제된 댓글입니다."),
    COMMENT_MODIFY_FORBIDDEN(3003, "작성자가 아니면 댓글을 수정할 수 없습니다."),
    COMMENT_DELETE_FORBIDDEN(3003, "작성자가 아니면 댓글을 삭제할 수 없습니다.")
}
