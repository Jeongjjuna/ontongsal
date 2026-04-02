package yjh.ontongsal.restapi

import java.time.Instant

class AuditInfo(
    val createdAt: Instant,
    var updatedAt: Instant? = null,
    val createdBy: String? = null,
    var updatedBy: String? = null,
)
