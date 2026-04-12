package yjh.ontongsal.restapi.adapter.persistence.jpa.entity

import jakarta.persistence.*
import yjh.ontongsal.restapi.domain.*

@Entity
@Table(name = "comments")
class CommentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val articleId: Long,

    @Column(nullable = false)
    val authorId: Long,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: CommentStatus

) : BaseEntity() {

    companion object {
        fun new(actor: Actor, article: Article, commentContent: CommentContent): CommentEntity {
            return CommentEntity(
                id = 0,
                articleId = article.id,
                authorId = actor.userId,
                content = commentContent.contentText,
                status = CommentStatus.ACTIVE
            )
        }

        fun of(comment: Comment): CommentEntity {
            return CommentEntity(
                id = comment.id,
                articleId = comment.articleId,
                authorId = comment.authorId,
                content = comment.content.contentText,
                status = comment.status
            )
        }
    }

    fun toComment(): Comment {
        return Comment(
            id = id,
            articleId = articleId,
            authorId = authorId,
            content = CommentContent(content),
            status = status,
            auditInfo = this.toAuditInfo(),
        )
    }
}
