package yjh.ontongsal.api.post.presentation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import yjh.ontongsal.api.common.Response
import yjh.ontongsal.api.post.application.PostService
import yjh.ontongsal.api.post.presentation.request.PostCreateRequest
import yjh.ontongsal.api.post.presentation.request.PostUpdateRequest
import yjh.ontongsal.api.post.presentation.request.toCommand
import yjh.ontongsal.api.post.presentation.response.PostResponse

@RestController
class PostController(
    private val postService: PostService,
) {

    @PostMapping("/posts")
    fun create(
        @RequestBody request: PostCreateRequest,
    ): ResponseEntity<Response<Long>> {
        return ResponseEntity.ok(Response(data = postService.create(request.toCommand())))
    }

    @PutMapping("/posts/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: PostUpdateRequest,
    ): ResponseEntity<Response<Long>> {
        return ResponseEntity.ok(Response(data = postService.update(id, request.toCommand())))
    }

    @DeleteMapping("/posts/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Response<Long>> {
        return ResponseEntity.ok(Response(data = postService.delete(id)))
    }

    @GetMapping("/posts/{id}")
    fun retrieve(
        @PathVariable id: Long,
    ): ResponseEntity<Response<PostResponse>> {
        val response = PostResponse.from(postService.retrieve(id))
        return ResponseEntity.ok(Response(data = response))
    }

    @GetMapping("/posts")
    fun retrieveAll(
        pageable: Pageable,
    ): ResponseEntity<Response<Page<PostResponse>>> {
        val response = postService.retrieveAll(pageable).map { PostResponse.from(it) }
        return ResponseEntity.ok(Response(data = response))
    }
}
