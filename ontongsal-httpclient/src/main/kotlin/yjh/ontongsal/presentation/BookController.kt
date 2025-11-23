package yjh.ontongsal.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class BookController {

    @GetMapping("/v1/books")
    fun getBooks(): ResponseEntity<List<String>> {
        // 테스트용 임의 응답
        return ResponseEntity.ok(
            listOf(
                "Spring in Action",
                "Kotlin Programming",
                "Effective Java"
            )
        )
    }
}
