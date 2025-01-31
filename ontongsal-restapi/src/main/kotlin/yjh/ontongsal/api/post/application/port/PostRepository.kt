package yjh.ontongsal.api.post.application.port

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import yjh.ontongsal.api.post.domain.Post

interface PostRepository {
    fun save(post: Post): Post

    fun findByIdOrNull(id: Long): Post?

    fun findAll(pageable: Pageable): Page<Post>
}
