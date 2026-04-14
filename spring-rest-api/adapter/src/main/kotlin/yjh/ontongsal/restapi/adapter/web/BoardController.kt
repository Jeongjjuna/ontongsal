package yjh.ontongsal.restapi.adapter.web

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yjh.ontongsal.restapi.adapter.web.dto.BoardResponse
import yjh.ontongsal.restapi.adapter.web.dto.CreateBoardRequest
import yjh.ontongsal.restapi.adapter.web.dto.UpdateBoardRequest
import yjh.ontongsal.restapi.adapter.web.support.LocationUriBuilder
import yjh.ontongsal.restapi.application.usecase.BoardUseCase
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.TargetId

@RestController
class BoardController(
    private val boardUseCase: BoardUseCase,
) {

    @PostMapping("/v1/admin/boards")
    fun createBoard(
        actor: Actor,
        @Valid @RequestBody request: CreateBoardRequest,
    ): ResponseEntity<Unit> {
        val successId = boardUseCase.create(actor, request.toBoardName())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @PutMapping("/v1/admin/boards/{id}")
    fun updateBoard(
        actor: Actor,
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateBoardRequest,
    ): ResponseEntity<Unit> {
        val successId = boardUseCase.update(actor, TargetId(id), request.toBoardName())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @DeleteMapping("/v1/admin/boards/{id}")
    fun deleteBoard(
        actor: Actor,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        boardUseCase.delete(actor, TargetId(id))
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/v1/admin/boards/{id}")
    fun getBoard(
        @PathVariable id: Long,
    ): ResponseEntity<BoardResponse> {
        val board = boardUseCase.findBoard(TargetId(id))
        return ResponseEntity.ok(BoardResponse.of(board))
    }

    @GetMapping("/v1/admin/boards")
    fun getBoards(): ResponseEntity<List<BoardResponse>> {
        val boards = boardUseCase.findAll()
        return ResponseEntity.ok(BoardResponse.of(boards))
    }
}
