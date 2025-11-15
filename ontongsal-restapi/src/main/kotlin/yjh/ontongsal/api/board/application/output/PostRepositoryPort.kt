package yjh.ontongsal.api.board.application.output

import yjh.ontongsal.api.board.domain.post.Post

interface PostRepositoryPort {
    fun findById(id: Long): Post?
}
