package yjh.ontongsal.restapi.port

import yjh.ontongsal.restapi.Article

interface LoadArticlePort {
    fun findById(id: Long): Article?
}
