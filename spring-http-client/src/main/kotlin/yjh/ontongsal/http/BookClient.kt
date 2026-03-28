package yjh.ontongsal.http

import org.springframework.web.service.annotation.GetExchange
import yjh.ontongsal.config.HttpClient
import yjh.ontongsal.config.HttpClientType

@HttpClient(url = "http://localhost:8080/api", type = HttpClientType.REST_CLIENT)
interface BookClient {

    @GetExchange("/v1/books")
    fun getBooks(): List<String>
}
