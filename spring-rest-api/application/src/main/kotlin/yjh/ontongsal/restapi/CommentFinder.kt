package yjh.ontongsal.restapi

import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.port.CommentRepository
import yjh.ontongsal.restapi.support.Slice

@Component
class CommentFinder(
    private val commentRepository: CommentRepository
) {
    fun find(articleId: TargetId, offset: Int, limit: Int): Slice<Comment> {
        return commentRepository.findAll(articleId, offset, limit)
    }
}
