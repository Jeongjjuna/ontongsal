package yjh.ontongsal.restapi.application.usecase

import yjh.ontongsal.restapi.domain.ArticleContent

interface ArticleUseCase {
    fun write(userId: Long, toContent: ArticleContent): Long
}
