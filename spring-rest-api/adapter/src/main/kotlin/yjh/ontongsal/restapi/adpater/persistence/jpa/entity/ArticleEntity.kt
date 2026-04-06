package yjh.ontongsal.restapi.adpater.persistence.jpa.entity

import jakarta.persistence.*
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.ArticleTitle
import java.time.Instant

@Entity
@Table(name = "articles")
class ArticleEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    val id: Long = 0,

    @Column(name = "title", nullable = false, length = 100)
    var title: String,

    @Column(name = "content", nullable = false, length = 3000)
    var content: String,

    @Column(name = "board_id", nullable = false)
    val boardId: Long,

    @Column(name = "writer_id", nullable = false)
    val writerId: Long,

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null,

    ) : BaseEntity() {

    companion object {
        fun new(actor: Actor, boardId: Long, title: ArticleTitle, content: ArticleContent): ArticleEntity {
            return ArticleEntity(
                id = 0,
                title = title.contentText,
                content = content.contentText,
                boardId = boardId,
                writerId = actor.userId,
            )
        }

        fun of(article: Article): ArticleEntity {
            return ArticleEntity(
                id = article.id,
                title = article.title.contentText,
                content = article.content.contentText,
                boardId = article.boardId,
                writerId = article.writerId,
                deletedAt = article.deletedAt,
            )
        }
    }

    fun toArticle(): Article {
        return Article(
            id = id,
            writerId = writerId,
            boardId = boardId,
            title = ArticleTitle(title),
            content = ArticleContent(content),
            auditInfo = this.toAuditInfo(),
        )
    }
}
