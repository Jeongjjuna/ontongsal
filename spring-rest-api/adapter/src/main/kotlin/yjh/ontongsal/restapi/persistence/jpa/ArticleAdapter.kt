package yjh.ontongsal.restapi.persistence.jpa

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.ontongsal.restapi.Article
import yjh.ontongsal.restapi.ArticleContent
import yjh.ontongsal.restapi.TargetId
import yjh.ontongsal.restapi.persistence.jpa.entity.ArticleEntity
import yjh.ontongsal.restapi.persistence.jpa.repository.ArticleJpaRepository
import yjh.ontongsal.restapi.port.ArticleRepository

@Repository
class ArticleAdapter(
    private val articleJpaRepository: ArticleJpaRepository,
) : ArticleRepository {

    override fun findById(targetId: TargetId): Article? {
        return articleJpaRepository.findByIdOrNull(targetId.targetId)?.toDomain()
    }

    fun ArticleEntity.toDomain(): Article =
        Article(
            id = id,
            authorId = authorId,
            content = ArticleContent(content),
            auditInfo = toAuditInfo(),
        )
}
