package yjh.ontongsal.api.post.application

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import yjh.ontongsal.api.post.application.port.PostRepository
import yjh.ontongsal.api.post.domain.PostCreateCommand

@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : DescribeSpec({

    describe("게시글 생성 시") {
        describe("게시글 생성 요청이 정상적으로 들어오면") {
            val postId = postService.create(
                PostCreateCommand(
                    title = "제목",
                    content = "내용"
                )
            )
            it("게시글 생성 ID가 반환 된다.") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdAt shouldNotBe null
                post?.updatedAt shouldNotBe null
            }
        }
    }
})
