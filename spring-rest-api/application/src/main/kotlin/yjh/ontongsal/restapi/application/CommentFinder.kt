package yjh.ontongsal.restapi.application

import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.CommentRepository
import yjh.ontongsal.restapi.domain.Comment
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Slice

@Component
class CommentFinder(
    private val commentRepository: CommentRepository
) {
    fun find(articleId: TargetId, offset: Int, limit: Int): Slice<Comment> {
        return commentRepository.findAll(articleId, offset, limit)
    }
}
