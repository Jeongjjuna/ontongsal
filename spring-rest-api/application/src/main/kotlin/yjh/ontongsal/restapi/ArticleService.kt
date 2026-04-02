package yjh.ontongsal.restapi

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.usecase.ArticleUseCase

private val logger = KotlinLogging.logger {}

@Service
class ArticleService(

): ArticleUseCase {

    override fun write(userId: Long, toContent: ArticleContent): Long {
        TODO("Not yet implemented")
    }
}
