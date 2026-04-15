CREATE TABLE boards
(
    board_id   BIGINT      NOT NULL COMMENT '게시판 PK',
    name       VARCHAR(50) NOT NULL COMMENT '게시판 이름',
    status     VARCHAR(20) NOT NULL COMMENT '게시판 상태 (ACTIVE / INACTIVE)',
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
    updated_by VARCHAR(50) NOT NULL COMMENT '수정자',

    PRIMARY KEY (board_id)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='게시판 테이블';

CREATE TABLE articles
(
    article_id BIGINT        NOT NULL COMMENT '게시글 PK',
    title      VARCHAR(100)  NOT NULL COMMENT '게시글 제목',
    content    VARCHAR(3000) NOT NULL COMMENT '게시글 내용',
    board_id   BIGINT        NOT NULL COMMENT '게시판 ID',
    writer_id  BIGINT        NOT NULL COMMENT '작성자 사용자 ID',
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    created_by VARCHAR(50)   NOT NULL COMMENT '생성자',
    updated_at TIMESTAMP     NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
    updated_by VARCHAR(50)   NULL COMMENT '수정자',
    deleted_at TIMESTAMP     NULL COMMENT '삭제 시간 (Soft Delete)',

    PRIMARY KEY (article_id),

    INDEX idx_board_id_article_id (board_id asc, article_id desc),
    INDEX idx_writer_id (writer_id)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='게시글 테이블';

CREATE TABLE comments
(
    comment_id BIGINT      NOT NULL COMMENT '댓글 PK',
    article_id BIGINT      NOT NULL COMMENT '게시글 ID',
    author_id  BIGINT      NOT NULL COMMENT '작성자 사용자 ID',
    content    TEXT        NOT NULL COMMENT '댓글 내용',
    status     VARCHAR(20) NOT NULL COMMENT '댓글 상태 (ACTIVE / DELETED)',
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
    updated_by VARCHAR(50) NOT NULL COMMENT '수정자',

    PRIMARY KEY (comment_id),

    INDEX idx_article_id_comment_id (article_id ASC, comment_id DESC)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='댓글 테이블';
