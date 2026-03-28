package yjh.ontongsal.presentation

import kotlin.collections.List
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class CarController {

    @GetMapping("/v1/cars")
    fun getCars(): ResponseEntity<List<String>> {
        // 테스트용 임의 응답

        return ResponseEntity.badRequest().build()
//        return ResponseEntity.ok(
//            listOf(
//                "Tesla Model S",
//                "BMW i8",
//                "Audi Q7",
//                "Hyundai Ioniq 5"
//            )
//        )
    }
}
