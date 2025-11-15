package yjh.ontongsal.api.board.domain

import java.time.Instant

class AuditInfo(
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant? = null,
) {
    fun update() {
        updatedAt = Instant.now()
    }
}
