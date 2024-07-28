package yjh.ontongsal.api.post.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class PostBehaviorSpecTest : BehaviorSpec({

    Context("게시글 수정 테스트") {

        Given("게시글과 수정커맨드가 주어지고") {
            val post = Post(title = "제목", content = "내용")
            val postUpdateCommand = PostUpdateCommand(title = "제목2", content = "내용2")

            When("수정 하면") {
                post.update(postUpdateCommand)

                Then("게시글 수정 성공") {
                    post.title shouldBe "제목2"
                    post.content shouldBe "내용2"
                }
            }
        }
    }

    Context("게시글 삭제 테스트") {

        Given("게시글이 주어지고") {
            val post = Post(title = "제목", content = "내용")

            When("삭제 하면") {
                post.delete()

                Then("deletedAt가 null이 아니다.") {
                    post.deletedAt.shouldNotBeNull()
                }
            }
        }
    }
})
