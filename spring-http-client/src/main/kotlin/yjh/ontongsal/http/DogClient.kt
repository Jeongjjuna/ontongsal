package yjh.ontongsal.http

import org.springframework.web.service.annotation.GetExchange
import reactor.core.publisher.Mono

interface DogClient {

    @GetExchange("/v1/dogs")
    fun getDogsAsync(): Mono<List<String>>

    @GetExchange("/v1/dogs")
    fun getDogs(): List<String>
}
