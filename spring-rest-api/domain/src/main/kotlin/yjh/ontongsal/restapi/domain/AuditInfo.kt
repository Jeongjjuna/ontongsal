package yjh.ontongsal.restapi.domain

import java.time.Instant

class AuditInfo(
    val createdAt: Instant,
    val createdBy: String,
    var updatedAt: Instant,
    var updatedBy: String,
)
