package yjh.ontongsal.restapi.application.usecase

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.ArticleTitle
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Page
import yjh.ontongsal.restapi.domain.support.Slice

interface ArticleUseCase {
    fun write(actor: Actor, boardId: TargetId, title: ArticleTitle, content: ArticleContent): Long
    fun update(actor: Actor, articleId: TargetId, title: ArticleTitle, content: ArticleContent): Long
    fun delete(actor: Actor, articleId: TargetId)

    fun findArticle(articleId: TargetId): Article
    fun findAll(targetId: TargetId, page: Int, pageSize: Int): Page<Article>
    fun findAllInfiniteScroll(targetId: TargetId, lastArticleId: Long?, pageSize: Int): Slice<Article>
}
