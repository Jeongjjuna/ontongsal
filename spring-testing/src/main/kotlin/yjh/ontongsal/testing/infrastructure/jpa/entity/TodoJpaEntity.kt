package yjh.ontongsal.testing.infrastructure.jpa.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.ontongsal.testing.domain.Todo
import java.time.Instant

@Entity
@Table(name = "todos")
@EntityListeners(AuditingEntityListener::class)
class TodoJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var content: String? = null,

    @Column(nullable = false)
    var completed: Boolean = false,

    @CreatedDate
    @Column(nullable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: Instant? = null,
) {
    fun toDomain(): Todo = Todo(
        id = id,
        userId = userId,
        title = title,
        content = content,
        completed = completed,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    companion object {
        fun fromDomain(domain: Todo): TodoJpaEntity = TodoJpaEntity(
            id = domain.id,
            userId = domain.userId,
            title = domain.title,
            content = domain.content,
            completed = domain.completed,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }
}
