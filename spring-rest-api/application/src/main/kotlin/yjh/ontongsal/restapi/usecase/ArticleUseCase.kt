package yjh.ontongsal.restapi.usecase

import yjh.ontongsal.restapi.ArticleContent

interface ArticleUseCase {
    fun write(userId: Long, toContent: ArticleContent): Long
}
