package yjh.ontongsal.restapi.domain.exception


enum class ErrorCode(
    val code: Int,
    val message: String,
) {
    // User (1000~1999)
    USER_NOT_FOUND(1000, "사용자를 찾을 수 없습니다"),
    USER_CONFLICT(1001, "사용자 충돌 발생"),

    // Post (2000~2999)
    ARTICLE_NOT_FOUND(2000, "게시글을 찾을 수 없습니다"),
    ARTICLE_CONFLICT(2001, "게시글 충돌 발생"),
    ARTICLE_DELETED(2002, "삭제된 게시글입니다."),
    ARTICLE_MODIFY_FORBIDDEN(2003, "작성자가 아니면 게시글을 수정할 수 없습니다."),
    ARTICLE_DELETE_FORBIDDEN(2004, "작성자가 아니면 게시글을 삭제할 수 없습니다."),

    // Comment (3000~3999)
    COMMENT_NOT_FOUND(3000, "댓글을 찾을 수 없습니다"),
    COMMENT_CONFLICT(3001, "댓글 충돌 발생"),
    COMMENT_DELETED(3002, "삭제된 댓글입니다."),
    COMMENT_MODIFY_FORBIDDEN(3003, "작성자가 아니면 댓글을 수정할 수 없습니다."),
    COMMENT_DELETE_FORBIDDEN(3004, "작성자가 아니면 댓글을 삭제할 수 없습니다."),

    // Board (4000~4999)
    BOARD_NOT_FOUND(4000, "게시판을 찾을 수 없습니다"),
    BOARD_CONFLICT(4001, "게시판 충돌 발생"),
    BOARD_DELETED(4002, "삭제된 게시판입니다."),
    BOARD_MODIFY_FORBIDDEN(4003, "관리자가 아니면 게시판을 수정할 수 없습니다."),
    BOARD_DELETE_FORBIDDEN(4004, "관리자가 아니면 게시판을 삭제할 수 없습니다.")
}
