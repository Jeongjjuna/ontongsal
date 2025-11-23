package yjh.ontongsal.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import yjh.ontongsal.http.BookClient
import yjh.ontongsal.http.CarClient

@RestController
class SimpleController(
    private val bookClient: BookClient,
    private val carClient: CarClient,
) {

    @GetMapping("/test")
    fun httpClientCallTest(): String {

        println("--------------------")
        val books = bookClient.getBooks()
        println("--------------------")
        val cars = carClient.getCars()
        println("--------------------")

        return "success"
    }
}
