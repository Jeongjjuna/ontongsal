package yjh.ontongsal.restapi.adapter.web

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yjh.ontongsal.restapi.application.usecase.CommentUseCase
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.adapter.web.dto.CommentCreateRequest
import yjh.ontongsal.restapi.adapter.web.dto.CommentResponse
import yjh.ontongsal.restapi.adapter.web.dto.CommentUpdateRequest
import yjh.ontongsal.restapi.adapter.web.dto.SliceResponse
import yjh.ontongsal.restapi.adapter.web.support.LocationUriBuilder

/* java -> public static final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER") */
private val customLogger = KotlinLogging.logger("CUSTOM_LOGGER")

@RestController
class CommentController(
    private val commentUseCase: CommentUseCase,
) {

    @PostMapping("/v1/articles/{id}/comments")
    fun createComment(
        actor: Actor,
        @PathVariable id: Long,
        @Valid @RequestBody request: CommentCreateRequest,
    ): ResponseEntity<Unit> {
        val successId = commentUseCase.write(actor, TargetId(id), request.toContent())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @PutMapping("/v1/comments/{id}")
    fun modifyComment(
        actor: Actor,
        @PathVariable id: Long,
        @Valid @RequestBody request: CommentUpdateRequest,
    ): ResponseEntity<Unit> {
        val successId = commentUseCase.update(actor, TargetId(id), request.toContent())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @DeleteMapping("/v1/comments/{id}")
    fun deleteComment(
        actor: Actor,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        commentUseCase.delete(actor, TargetId(id))
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/v1/comments")
    fun getComments(
        @RequestParam articleId: Long,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ResponseEntity<SliceResponse<CommentResponse>> {
        customLogger.info { "custom logger example ..." }

        val slice = commentUseCase.findComments(TargetId(articleId), offset, limit)
        return ResponseEntity.ok(SliceResponse(CommentResponse.of(slice.content), slice.hasNext))
    }
}


