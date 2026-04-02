package yjh.ontongsal.restapi.port

import yjh.ontongsal.restapi.Article
import yjh.ontongsal.restapi.TargetId

interface ArticleRepository {
    fun findById(targetId: TargetId): Article?
}
