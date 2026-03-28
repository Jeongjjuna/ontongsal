package yjh.ontongsal.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class DogController {

    @GetMapping("/v1/dogs")
    fun getCars(): ResponseEntity<List<String>> {
        // 테스트용 임의 응답
        return ResponseEntity.internalServerError().build()
        // return ResponseEntity.badRequest().build()
//        Thread.sleep(5000)
//        return ResponseEntity.ok(
//            listOf(
//                "불독",
//                "시바이누",
//                "리트리버",
//                "푸들",
//                "비숑 프리제",
//                "코기",
//                "말티즈"
//            )
//        )
    }
}
