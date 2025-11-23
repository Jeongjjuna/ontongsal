package yjh.ontongsal.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import yjh.ontongsal.http.BookClient
import yjh.ontongsal.http.CarClient
import yjh.ontongsal.http.DogClient

@RestController
class SimpleController(
    private val bookClient: BookClient,
    private val carClient: CarClient,
    private val dogClient: DogClient,
) {

    @GetMapping("/test1")
    fun httpClientCallTest1(): String {

        println("--------------------")
        val books = bookClient.getBooks()
        println("--------------------")
        val cars = carClient.getCars()
        println("--------------------")

        return "success"
    }

    @GetMapping("/test2")
    fun httpClientCallTest2(): String {

        println("--------------------")
        val dogs: Mono<List<String>> = dogClient.getDogsAsync()
        dogs.subscribe(
            { list -> println("✅ 응답 도착! $list") },
            { error -> println("❌ 에러 발생: $error") },
            { println("🎉 완료" + Thread.currentThread().name) }
        )
        println("--------------------" + Thread.currentThread().name)

        return "success"
    }

    @GetMapping("/test3")
    fun httpClientCallTest3(): String {

        println("--------------------")
        val dogs: List<String> = dogClient.getDogs()
        println("--------------------" + Thread.currentThread().name)

        return "success"
    }
}
