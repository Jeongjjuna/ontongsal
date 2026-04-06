package yjh.ontongsal.restapi.adpater.persistence.jpa

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.ontongsal.restapi.adpater.persistence.jpa.entity.ArticleEntity
import yjh.ontongsal.restapi.adpater.persistence.jpa.repository.ArticleJpaRepository
import yjh.ontongsal.restapi.adpater.persistence.jpa.support.OffsetLimit
import yjh.ontongsal.restapi.application.port.ArticleRepository
import yjh.ontongsal.restapi.domain.*
import yjh.ontongsal.restapi.domain.support.Page
import yjh.ontongsal.restapi.domain.support.Slice

@Repository
class ArticleAdapter(
    private val articleJpaRepository: ArticleJpaRepository,
) : ArticleRepository {

    override fun save(actor: Actor, boardId: Long, title: ArticleTitle, content: ArticleContent): Article {
        val entity = ArticleEntity.new(actor, boardId, title, content)
        return articleJpaRepository.save(entity).toArticle()
    }

    override fun update(article: Article): Article {
        return articleJpaRepository.save(ArticleEntity.of(article)).toArticle()
    }

    override fun delete(article: Article) {
        articleJpaRepository.save(ArticleEntity.of(article))
    }

    override fun findById(targetId: TargetId): Article? {
        return articleJpaRepository.findByIdOrNull(targetId.targetId)?.toArticle()
    }

    override fun findAll(boardId: TargetId, page: Int, pageSize: Int): Page<Article> {
        val pageable = OffsetLimit(page, pageSize).toPageable()
        val result = articleJpaRepository.findAllByBoardIdForPage(boardId.targetId, pageable)
        return Page(
            content = result.content.map { it.toArticle() },
            page = page / pageSize,
            size = pageSize,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
            hasNext = result.hasNext(),
        )
    }

    override fun findAllInfiniteScroll(boardId: TargetId, pageSize: Int): Slice<Article> {
        val pageable = org.springframework.data.domain.PageRequest.of(0, pageSize)
        val result = articleJpaRepository.findAllByBoardIdOrderByIdDesc(boardId.targetId, pageable)
        return Slice(result.content.map { it.toArticle() }, result.hasNext())
    }

    override fun findAllInfiniteScroll(boardId: TargetId, pageSize: Int, lastArticleId: Long): Slice<Article> {
        val pageable = org.springframework.data.domain.PageRequest.of(0, pageSize)
        val result =
            articleJpaRepository.findAllByBoardIdAndIdLessThanOrderByIdDesc(boardId.targetId, lastArticleId, pageable)
        return Slice(result.content.map { it.toArticle() }, result.hasNext())
    }
}
