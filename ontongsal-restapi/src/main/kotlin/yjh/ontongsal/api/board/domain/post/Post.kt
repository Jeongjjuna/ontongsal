package yjh.ontongsal.api.board.domain.post

import yjh.ontongsal.api.board.domain.AuditInfo
import yjh.ontongsal.api.board.domain.Content

class Post(
    val id: Long = 0,
    val authorId: Long,
    val auditInfo: AuditInfo = AuditInfo(),
    val content: Content,
) {

    companion object {
        fun create(authorId: Long, content: String): Post {
            return Post(
                authorId = authorId,
                content = PostContent(content),
            )
        }
    }
}
