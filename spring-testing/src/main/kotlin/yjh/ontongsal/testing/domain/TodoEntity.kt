package yjh.ontongsal.testing.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "todos")
@EntityListeners(AuditingEntityListener::class)
class TodoEntity(
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
    fun update(title: String, content: String?, completed: Boolean) {
        this.title = title
        this.content = content
        this.completed = completed
    }
}
