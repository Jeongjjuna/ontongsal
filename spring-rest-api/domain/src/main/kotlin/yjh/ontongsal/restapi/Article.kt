package yjh.ontongsal.restapi

class Article(
    val id: Long = 0,
    val authorId: Long,
    val auditInfo: AuditInfo = AuditInfo(),
    val content: Content,
) {

    companion object {
        fun create(authorId: Long, content: String): Article {
            return Article(
                authorId = authorId,
                content = ArticleContent(content),
            )
        }
    }
}
