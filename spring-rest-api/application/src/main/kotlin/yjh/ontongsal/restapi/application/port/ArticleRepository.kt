package yjh.ontongsal.restapi.application.port

import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.TargetId

interface ArticleRepository {
    fun findById(targetId: TargetId): Article?
}
