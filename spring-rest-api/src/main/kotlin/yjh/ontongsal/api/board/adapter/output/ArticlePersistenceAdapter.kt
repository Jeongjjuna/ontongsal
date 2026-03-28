package yjh.ontongsal.api.board.adapter.output

import org.springframework.stereotype.Repository
import yjh.ontongsal.api.board.application.output.LoadArticlePort
import yjh.ontongsal.api.board.domain.AuditInfo
import yjh.ontongsal.api.board.domain.article.Article
import yjh.ontongsal.api.board.domain.article.ArticleContent

@Repository
class ArticlePersistenceAdapter : LoadArticlePort {
    override fun findById(id: Long): Article? {
        return Article(
            id = id,
            authorId = 999,
            content = ArticleContent("샘플 게시글 내용입니다."),
            auditInfo = AuditInfo()
        )
    }
}
