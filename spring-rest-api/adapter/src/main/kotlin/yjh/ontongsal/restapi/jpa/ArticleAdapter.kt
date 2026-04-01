package yjh.ontongsal.restapi.jpa

import org.springframework.stereotype.Repository
import yjh.ontongsal.restapi.port.LoadArticlePort
import yjh.ontongsal.restapi.AuditInfo
import yjh.ontongsal.restapi.Article
import yjh.ontongsal.restapi.ArticleContent

@Repository
class ArticleAdapter : LoadArticlePort {
    override fun findById(id: Long): Article? {
        return Article(
            id = id,
            authorId = 999,
            content = ArticleContent("샘플 게시글 내용입니다."),
            auditInfo = AuditInfo()
        )
    }
}
