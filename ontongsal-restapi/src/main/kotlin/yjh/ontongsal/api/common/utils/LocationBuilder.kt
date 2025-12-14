package yjh.ontongsal.api.common.utils

import java.net.URI
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

object LocationBuilder {
    fun fromCurrent(id: Long): URI =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
}
