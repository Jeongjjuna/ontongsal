package yjh.ontongsal.api.post.domain

import java.time.LocalDateTime

/**
 * 도메인 Post의 필드에 getter는 필요한 상황.
 *
 * val = getter o, setter x
 * var = getter o, setter o
 * private = 클래스 내부에서만 getter o, setter o
 * protected = 클래스 + 하위에서만 getter o, setter o
 *
 * 선택해야한다. -> 생성자 var로 사용하지만, setter를 사용하지 않을 것!
 */
class Post(
    val id: Long = 0,
    var title: String,
    var content: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun create(command: PostCreateCommand): Post {
            return Post(
                title = command.title,
                content = command.content
            )
        }
    }

    fun update(command: PostUpdateCommand) {
        this.title = command.title
        this.content = command.content
    }

    fun delete() {
        this.deletedAt = LocalDateTime.now()
    }
}
