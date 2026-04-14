package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.BoardRepository
import yjh.ontongsal.restapi.application.port.TransactionRunner
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName

private val logger = KotlinLogging.logger { }

@Component
class BoardCreator(
    private val boardRepository: BoardRepository,
    private val transaction: TransactionRunner,
) {

    fun create(actor: Actor, boardName: BoardName): Board {
        return transaction.run {
            boardRepository.save(actor, boardName)
        }.also {
            logger.info { "board created : boardId=${it.id}, managerId=${actor.userId}" }
        }
    }
}
