package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.application.usecase.ArticleUseCase
import yjh.ontongsal.restapi.domain.ArticleContent

private val logger = KotlinLogging.logger {}

@Service
class ArticleService(

): ArticleUseCase {

    override fun write(userId: Long, toContent: ArticleContent): Long {
        TODO("Not yet implemented")
    }
}
