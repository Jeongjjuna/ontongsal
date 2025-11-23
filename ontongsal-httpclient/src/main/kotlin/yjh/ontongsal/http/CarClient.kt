package yjh.ontongsal.http

import org.springframework.web.service.annotation.GetExchange

interface CarClient {

    @GetExchange("/v1/cars")
    fun getCars(): List<String>
}
