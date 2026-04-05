package yjh.ontongsal.restapi.adpater.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import yjh.ontongsal.restapi.application.usecase.ArticleUseCase
import yjh.ontongsal.restapi.adpater.web.dto.WriteArticleRequest
import yjh.ontongsal.restapi.adpater.web.support.LocationUriBuilder

@RestController
class ArticleController(
    private val articleUseCase: ArticleUseCase,
) {

    @PostMapping("v1/articles")
    fun writeArticle(
        @RequestBody request: WriteArticleRequest,
    ): ResponseEntity<Unit> {
        val successId = articleUseCase.write(999L, request.toContent())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }
}
