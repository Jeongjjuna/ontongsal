package yjh.ontongsal.api.post.infrastructure.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PostJpaRepository : JpaRepository<PostEntity, Long> {

    @Query("SELECT p FROM PostEntity p WHERE p.deletedAt IS NULL AND p.id = :id")
    fun findByIdOrNull(@Param("id") id: Long): PostEntity?

    @Query("SELECT p FROM PostEntity p WHERE p.deletedAt IS NULL")
    override fun findAll(pageable: Pageable): Page<PostEntity>
}
