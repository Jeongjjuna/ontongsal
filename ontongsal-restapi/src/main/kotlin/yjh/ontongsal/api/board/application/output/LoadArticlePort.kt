package yjh.ontongsal.api.board.application.output

import yjh.ontongsal.api.board.domain.article.Article

interface LoadArticlePort {
    fun findById(id: Long): Article?
}
