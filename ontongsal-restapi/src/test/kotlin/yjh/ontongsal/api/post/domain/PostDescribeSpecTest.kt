package yjh.ontongsal.api.post.domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class PostDescribeSpecTest : DescribeSpec({

    describe("게시글 수정 테스트") {
        val post = Post(title = "제목", content = "내용")

        describe("수정 요청 시") {
            val postUpdateCommand = PostUpdateCommand(title = "제목2", content = "내용2")
            post.update(postUpdateCommand)

            it("게시글이 변경된다.") {
                post.title shouldBe "제목2"
                post.content shouldBe "내용2"
            }
        }
    }

    describe("게시글 삭제 테스트") {
        val post = Post(title = "제목", content = "내용")

        describe("삭제 하면") {
            post.delete()

            it("deletedAt가 null 이 아니다.") {
                post.deletedAt.shouldNotBeNull()
            }
        }
    }
})
