package yjh.ontongsal.restapi.adpater.web.support

import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

object LocationUriBuilder {
    fun fromCurrent(id: Long): URI =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
}
