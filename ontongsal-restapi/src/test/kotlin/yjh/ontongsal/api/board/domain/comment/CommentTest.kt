package yjh.ontongsal.api.board.domain.comment

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import yjh.ontongsal.api.board.domain.Actor
import yjh.ontongsal.api.board.domain.post.Post
import yjh.ontongsal.api.common.exception.AppException

class CommentTest : DescribeSpec({

    val post = Post.create(authorId = 1L, content = "게시글 초기 내용")
    val author = Actor(id = 100L, userName = "작성자")

    describe("댓글 작성할 때") {
        it("처음 작성하면 상태는 활성이어야 한다") {
            val comment = Comment.create(post = post, author = author, content = "댓글 내용")
            comment.status shouldBe CommentStatus.ACTIVE
        }

        it("게시글, 작성자, 내용이 정확히 설정되어야 한다") {
            val comment = Comment.create(post = post, author = author, content = "댓글 내용")
            comment.postId shouldBe post.id
            comment.authorId shouldBe author.id
            comment.content.contentText shouldBe "댓글 내용"
        }
    }

    describe("댓글 활성화 여부 확인할 때") {
        it("활성와 여부를 알 수 있다.") {
            val comment = Comment.create(post = post, author = author, content = "댓글 내용")
            comment.isActive() shouldBe true
        }

        it("삭제 후 댓글은 활성 상태가 아니다") {
            val comment = Comment.create(post = post, author = author, content = "댓글 내용")
            comment.delete(author)
            comment.isActive() shouldBe false
        }
    }

    describe("댓글 내용 수정") {
        it("작성자가 아닌 경우 내용은 변경할 수 없다") {
            val comment = Comment.create(post = post, author = author, content = "내용")
            val otherActor = Actor(id = 999L, userName = "다른사람")
            shouldThrow<AppException.Unauthorized> {
                comment.updateContent("변경 내용", otherActor)
            }
        }

        it("삭제된 댓글은 내용을 변경할 수 없다") {
            val comment = Comment.create(post = post, author = author, content = "내용")
            comment.delete(author)
            shouldThrow<AppException.BadRequest> {
                comment.updateContent("변경 내용", author)
            }
        }

        it("작성자는 댓글 내용을 변경할 수 있다") {
            val comment = Comment.create(post = post, author = author, content = "내용")
            comment.updateContent("변경 내용", author)
            comment.content.contentText shouldBe "변경 내용"
        }
    }

    describe("댓글 삭제") {
        it("작성자가 아닌 경우 댓글은 삭제할 수 없다") {
            val comment = Comment.create(post = post, author = author, content = "내용")
            val otherActor = Actor(id = 999L, userName = "다른사람")
            shouldThrow<AppException.Unauthorized> {
                comment.delete(otherActor)
            }
        }

        it("작성자는 댓글을 삭제할 수 있으며, 상태는 비활성이어야 한다") {
            val comment = Comment.create(post = post, author = author, content = "내용")
            comment.delete(author)
            comment.isActive() shouldBe false
            comment.status shouldBe CommentStatus.DELETED
        }
    }
})
