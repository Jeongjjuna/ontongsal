package yjh.ontongsal.restapi.adapter.web

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yjh.ontongsal.restapi.adapter.web.dto.BoardResponse
import yjh.ontongsal.restapi.adapter.web.dto.CreateBoardRequest
import yjh.ontongsal.restapi.adapter.web.dto.PageResponse
import yjh.ontongsal.restapi.adapter.web.dto.UpdateBoardRequest
import yjh.ontongsal.restapi.adapter.web.support.LocationUriBuilder
import yjh.ontongsal.restapi.application.usecase.BoardUseCase
import yjh.ontongsal.restapi.domain.TargetId

@RestController
class BoardController(
    private val boardUseCase: BoardUseCase,
) {

    @PostMapping("/v1/admin/boards")
    fun createBoard(
        @Valid @RequestBody request: CreateBoardRequest,
    ): ResponseEntity<Unit> {
        val successId = boardUseCase.create(request.toBoardName())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @PutMapping("/v1/admin/boards/{id}")
    fun updateBoard(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateBoardRequest,
    ): ResponseEntity<Unit> {
        val successId = boardUseCase.update(TargetId(id), request.toBoardName())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @DeleteMapping("/v1/admin/boards/{id}")
    fun deleteBoard(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        boardUseCase.delete(TargetId(id))
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
    fun getBoards(
        @RequestParam page: Int,
        @RequestParam pageSize: Int,
    ): ResponseEntity<PageResponse<BoardResponse>> {
        val result = boardUseCase.findAll(page, pageSize)
        return ResponseEntity.ok(
            PageResponse(
                BoardResponse.of(result.content),
                result.page,
                result.size,
                result.totalElements,
                result.totalPages,
                result.hasNext,
            )
        )
    }
}
