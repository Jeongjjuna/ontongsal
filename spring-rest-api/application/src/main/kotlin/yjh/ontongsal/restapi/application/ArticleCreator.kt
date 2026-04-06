package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.ArticleRepository
import yjh.ontongsal.restapi.application.port.TransactionRunner
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.ArticleTitle
import yjh.ontongsal.restapi.domain.TargetId

private val logger = KotlinLogging.logger { }

@Component
class ArticleCreator(
    private val articleRepository: ArticleRepository,
    private val transaction: TransactionRunner,
) {

    fun create(
        actor: Actor,
        boardId: TargetId,
        title: ArticleTitle,
        content: ArticleContent,
    ): Article {
        return transaction.run {
            articleRepository.save(actor, boardId.targetId, title, content)
        }.also {
            logger.info { "article created : articleId=${it.id}, boardId=$boardId" }
        }
    }
}
