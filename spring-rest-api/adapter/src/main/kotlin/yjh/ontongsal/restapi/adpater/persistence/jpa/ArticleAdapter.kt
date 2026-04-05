package yjh.ontongsal.restapi.adpater.persistence.jpa

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.ontongsal.restapi.adpater.persistence.jpa.entity.ArticleEntity
import yjh.ontongsal.restapi.adpater.persistence.jpa.repository.ArticleJpaRepository
import yjh.ontongsal.restapi.application.port.ArticleRepository
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.TargetId

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
