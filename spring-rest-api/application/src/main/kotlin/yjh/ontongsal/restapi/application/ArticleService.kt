package yjh.ontongsal.restapi.application

import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.application.usecase.ArticleUseCase
import yjh.ontongsal.restapi.domain.*
import yjh.ontongsal.restapi.domain.support.Page
import yjh.ontongsal.restapi.domain.support.Slice

@Service
class ArticleCommandService(
    private val articleCreator: ArticleCreator,
    private val articleUpdater: ArticleUpdater,
    private val articleDeleter: ArticleDeleter,
    private val articleFinder: ArticleFinder,
    private val articleEventHandler: ArticleEventHandler,
) : ArticleUseCase {

    override fun write(actor: Actor, boardId: TargetId, title: ArticleTitle, content: ArticleContent): Long {
        val createdArticle = articleCreator.create(actor, boardId, title, content)
        articleEventHandler.created(actor, createdArticle)
        return createdArticle.id
    }

    override fun update(actor: Actor, articleId: TargetId, title: ArticleTitle, content: ArticleContent): Long {
        val updatedArticle = articleUpdater.update(actor, articleId, title, content)
        articleEventHandler.updated(actor, articleId, updatedArticle)
        return updatedArticle.id
    }

    override fun delete(actor: Actor, articleId: TargetId) {
        articleDeleter.delete(actor, articleId)
    }

    override fun findArticle(articleId: TargetId): Article {
        return articleFinder.find(articleId)
    }

    override fun findAll(
        targetId: TargetId,
        page: Int,
        pageSize: Int,
    ): Page<Article> {
        return articleFinder.findAll(targetId, page, pageSize)
    }

    override fun findAllInfiniteScroll(
        targetId: TargetId,
        lastArticleId: Long?,
        pageSize: Int,
    ): Slice<Article> {
        return articleFinder.findInfiniteScroll(targetId, lastArticleId, pageSize)
    }
}
