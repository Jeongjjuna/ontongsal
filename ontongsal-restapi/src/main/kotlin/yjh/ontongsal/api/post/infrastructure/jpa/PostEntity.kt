package yjh.ontongsal.api.post.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.ontongsal.api.post.domain.Post
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "posts")
@Entity
class PostEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "title", nullable = false)
    private val title: String,

    @Column(name = "content", nullable = false)
    private val content: String,

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private var createdAt: LocalDateTime?,

    @LastModifiedDate
    @Column(name = "updated_at")
    private var updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime? = null,
) {

    companion object {
        fun from(post: Post): PostEntity {
            return PostEntity(
                id = post.id,
                title = post.title,
                content = post.content,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
                deletedAt = post.deletedAt
            )
        }
    }

    fun toModel(): Post {
        return Post(
            id = this.id,
            title = this.title,
            content = this.content,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }
}
