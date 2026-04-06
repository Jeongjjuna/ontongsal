package yjh.ontongsal.restapi.adpater.persistence.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.ontongsal.restapi.domain.AuditInfo
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(

    // @CreatedDate
    @Column(nullable = false)
    var createdAt: Instant? = null,

    @Column(nullable = false, length = 50)
    @CreatedBy
    var createdBy: String? = null,

    // @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: Instant? = null,

    @Column(nullable = false, length = 50)
    @LastModifiedBy
    var updatedBy: String? = null,
) {
    protected fun toAuditInfo(): AuditInfo =
        AuditInfo(
            createdAt = requireNotNull(createdAt),
            updatedAt = requireNotNull(updatedAt),
            createdBy = requireNotNull(createdBy),
            updatedBy = requireNotNull(updatedBy),
        )
}
