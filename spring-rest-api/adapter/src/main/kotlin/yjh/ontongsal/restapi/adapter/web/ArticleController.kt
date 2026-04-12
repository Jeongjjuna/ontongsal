package yjh.ontongsal.restapi.adapter.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yjh.ontongsal.restapi.application.usecase.ArticleUseCase
import yjh.ontongsal.restapi.adapter.web.dto.ArticleResponse
import yjh.ontongsal.restapi.adapter.web.dto.PageResponse
import yjh.ontongsal.restapi.adapter.web.dto.SliceResponse
import yjh.ontongsal.restapi.adapter.web.dto.UpdateArticleRequest
import yjh.ontongsal.restapi.adapter.web.dto.WriteArticleRequest
import yjh.ontongsal.restapi.adapter.web.support.LocationUriBuilder
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.TargetId

@RestController
class ArticleController(
    private val articleUseCase: ArticleUseCase,
) {

    @PostMapping("/v1/articles")
    fun writeArticle(
        actor: Actor,
        @RequestBody request: WriteArticleRequest,
    ): ResponseEntity<Unit> {
        val successId = articleUseCase.write(actor, TargetId(request.boardId), request.toTitle(), request.toContent())

        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @PutMapping("/v1/articles/{id}")
    fun updateArticle(
        actor: Actor,
        @PathVariable id: Long,
        @RequestBody request: UpdateArticleRequest,
    ): ResponseEntity<Unit> {
        val successId = articleUseCase.update(actor, TargetId(id), request.toTitle(), request.toContent())

        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @DeleteMapping("/v1/articles/{id}")
    fun deleteArticle(
        actor: Actor,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        articleUseCase.delete(actor, TargetId(id))
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/v1/articles/{id}")
    fun getArticle(
        @PathVariable id: Long,
    ): ResponseEntity<ArticleResponse> {
        val article = articleUseCase.findArticle(TargetId(id))

        return ResponseEntity.ok(ArticleResponse.of(article))
    }

    /**
     * cursor 기반 무한스크롤
     */
    @GetMapping("/v1/articles")
    fun getAllInfiniteScroll(
        @RequestParam boardId: Long,
        @RequestParam(required = false) lastArticleId: Long?,
        @RequestParam pageSize: Int,
    ): ResponseEntity<SliceResponse<ArticleResponse>> {
        val slice = articleUseCase.findAllInfiniteScroll(TargetId(boardId), lastArticleId, pageSize)

        return ResponseEntity.ok(
            SliceResponse(
                ArticleResponse.of(slice.content),
                slice.hasNext
            )
        )
    }

    /**
     * offset 기반 페이지네이션(어드민에서 주로 사용)
     */
    @GetMapping("/v1/admin/articles")
    fun getArticles(
        @RequestParam boardId: Long,
        @RequestParam page: Int,
        @RequestParam pageSize: Int,
    ): ResponseEntity<PageResponse<ArticleResponse>> {
        val page = articleUseCase.findAll(TargetId(boardId), page, pageSize)

        return ResponseEntity.ok(
            PageResponse(
                ArticleResponse.of(articles = page.content),
                page.page,
                page.size,
                page.totalElements,
                page.totalPages,
                page.hasNext
            )
        )
    }

}
