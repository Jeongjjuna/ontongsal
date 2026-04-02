package yjh.ontongsal.restapi

class Article(
    val id: Long,
    val authorId: Long,
    val content: Content,
    val auditInfo: AuditInfo?,
)
