package yjh.ontongsal.restapi.domain

class Article(
    val id: Long,
    val authorId: Long,
    val content: Content,
    val auditInfo: AuditInfo?,
)
