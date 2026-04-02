package yjh.ontongsal.restapi.persistence.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.ontongsal.restapi.AuditInfo
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    var updatedAt: Instant? = null,

    @CreatedBy
    @Column(nullable = false, updatable = false)
    var createdBy: String? = null,

    @LastModifiedBy
    var updatedBy: String? = null,
) {
    fun toAuditInfo(): AuditInfo =
        AuditInfo(
            createdAt = requireNotNull(createdAt),
            updatedAt = updatedAt,
            createdBy = createdBy,
            updatedBy = updatedBy,
        )
}
