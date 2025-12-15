package yjh.ontongsal.api.board.adapter.input.rest

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import yjh.ontongsal.api.board.adapter.input.rest.dto.CommentCreateRequest
import yjh.ontongsal.api.board.adapter.input.rest.dto.toCommand
import yjh.ontongsal.api.board.application.input.DeleteCommentUseCase
import yjh.ontongsal.api.board.application.input.GetCommentUseCase
import yjh.ontongsal.api.board.application.input.UpdateCommentUseCase
import yjh.ontongsal.api.board.application.input.WriteCommentUseCase
import yjh.ontongsal.api.board.domain.comment.Comment
import yjh.ontongsal.api.common.utils.LocationBuilder

/* java -> public static final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER") */
private val customLogger = KotlinLogging.logger("CUSTOM_LOGGER")

@RestController
class CommentController(
    private val writeCommentUseCase: WriteCommentUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val getCommentUseCase: GetCommentUseCase,
) {

    @PostMapping("/v1/articles/{id}/comments")
    fun create(
        @PathVariable id: Long,
        @Valid @RequestBody request: CommentCreateRequest,
    ): ResponseEntity<Unit> {
        val command = request.toCommand(articleId = id, authorId = 123, authorName = "홍길동")
        val commentId = writeCommentUseCase.write(command)

        return ResponseEntity
            .created(LocationBuilder.fromCurrent(commentId))
            .build()
    }

    // throw AppException.NotFound(ErrorCode.POST_NOT_FOUND, "이거 찾을 수 없어요 : ${1}")
    @GetMapping("/v1/comments/{id}")
    fun retrieve(@PathVariable id: Long): ResponseEntity<Comment> {
        customLogger.info { "custom logger example ..." }
        return ResponseEntity.ok(getCommentUseCase.getComment(id))
    }
}


