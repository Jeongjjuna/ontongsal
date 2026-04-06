package yjh.ontongsal.restapi.application

import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.ArticleRepository
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode
import yjh.ontongsal.restapi.domain.support.Page
import yjh.ontongsal.restapi.domain.support.Slice

@Component
class ArticleFinder(
    private val articleRepository: ArticleRepository,
) {

    fun find(articleId: TargetId): Article {
        return articleRepository.findById(articleId)
            ?: throw AppException.NotFound(ErrorCode.ARTICLE_NOT_FOUND, "게시글을 찾을 수 없습니다. [$articleId]")
    }

    fun findAll(boardId: TargetId, page: Int, pageSize: Int): Page<Article> {
        return articleRepository.findAll(boardId, page, pageSize)
    }

    fun findInfiniteScroll(boardId: TargetId, lastArticleId: Long?, pageSize: Int): Slice<Article> {
        return if (lastArticleId == null) {
            articleRepository.findAllInfiniteScroll(boardId, pageSize)
        } else {
            articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId)
        }
    }
}
