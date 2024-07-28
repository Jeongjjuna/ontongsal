package yjh.ontongsal.api.post.infrastructure

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.ontongsal.api.post.application.port.PostRepository
import yjh.ontongsal.api.post.domain.Post
import yjh.ontongsal.api.post.infrastructure.jooq.PostJooqRepository
import yjh.ontongsal.api.post.infrastructure.jpa.PostEntity
import yjh.ontongsal.api.post.infrastructure.jpa.PostJpaRepository

@Repository
class PostRepositoryAdapter(
    private val postJpaRepository: PostJpaRepository,
    private val postJooqRepository: PostJooqRepository,
) : PostRepository {
    override fun save(post: Post): Post {
        return postJpaRepository.save(PostEntity.from(post)).toModel()
    }

    override fun findByIdOrNull(id: Long): Post? {
        return postJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun findAll(pageable: Pageable): Page<Post> {
        return postJpaRepository.findAll(pageable).map { it.toModel() }
    }
}
