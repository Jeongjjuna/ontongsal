package yjh.ontongsal.api.board.adapter.input.rest

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import yjh.ontongsal.api.board.application.input.CommentUsecase
import yjh.ontongsal.api.board.domain.comment.Comment

/* java -> public static final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER") */
private val customLogger = KotlinLogging.logger("CUSTOM_LOGGER")

@RestController
class CommentController(
    private val commentUsecase: CommentUsecase,
) {

    @PostMapping("/v1/posts/{postId}/comments")
    fun create(
        @PathVariable postId: Long,
        @RequestBody request: CommentCreateRequest,
    ): ResponseEntity<Unit> {

        val command = request.toCommand(postId = postId, authorId = 123, authorName = "홍길동")
        val savedComment = commentUsecase.write(command)

        val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(savedComment.id)
            .toUri()

        return ResponseEntity
            .created(uri)
            .build()
    }

    // throw AppException.NotFound(ErrorCode.POST_NOT_FOUND, "이거 찾을 수 없어요 : ${1}")
    @GetMapping("/v1/comments/{id}")
    fun retrieve(@PathVariable id: Long): ResponseEntity<Comment> {
        customLogger.info { "custom logger example ..." }
        return ResponseEntity.ok(commentUsecase.read(id))
    }
}


