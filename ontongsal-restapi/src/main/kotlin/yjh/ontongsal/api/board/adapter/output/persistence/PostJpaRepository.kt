package yjh.ontongsal.api.board.adapter.output.persistence

import org.springframework.stereotype.Repository
import yjh.ontongsal.api.board.application.output.PostRepositoryPort
import yjh.ontongsal.api.board.domain.AuditInfo
import yjh.ontongsal.api.board.domain.post.Post
import yjh.ontongsal.api.board.domain.post.PostContent

@Repository
class PostJpaRepository : PostRepositoryPort {
    override fun findById(id: Long): Post? {
        println("bbbb")
        return Post(
            id = id,
            authorId = 999,
            content = PostContent("샘플 게시글 내용입니다."),
            auditInfo = AuditInfo()
        )
    }
}
