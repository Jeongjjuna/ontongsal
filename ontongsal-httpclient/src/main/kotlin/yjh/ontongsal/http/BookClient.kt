package yjh.ontongsal.http

import org.springframework.web.service.annotation.GetExchange

interface BookClient {

    @GetExchange("/v1/books")
    fun getBooks(): List<String>
}
