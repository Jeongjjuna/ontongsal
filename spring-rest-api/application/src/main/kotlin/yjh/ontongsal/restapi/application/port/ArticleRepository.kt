package yjh.ontongsal.restapi.application.port

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.ArticleTitle
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Page
import yjh.ontongsal.restapi.domain.support.Slice

interface ArticleRepository {
    fun save(actor: Actor, boardId: Long, title: ArticleTitle, content: ArticleContent): Article
    fun update(article: Article): Article
    fun delete(article: Article)
    fun findById(targetId: TargetId): Article?
    fun findAll(boardId: TargetId, page: Int, pageSize: Int): Page<Article>
    fun findAllInfiniteScroll(boardId: TargetId, pageSize: Int): Slice<Article>
    fun findAllInfiniteScroll(boardId: TargetId, pageSize: Int, lastArticleId: Long): Slice<Article>
}
