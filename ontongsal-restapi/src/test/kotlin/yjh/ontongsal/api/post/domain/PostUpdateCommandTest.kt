//package yjh.ontongsal.api.post.domain
//
//import io.kotest.assertions.throwables.shouldNotThrowAny
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.DescribeSpec
//import io.kotest.matchers.shouldBe
//import org.springframework.http.HttpStatus
//import yjh.ontongsal.api.common.BaseException
//
//class PostUpdateCommandTest : DescribeSpec({
//
//    describe("생성 테스트") {
//        val validTitle = "유효한 제목"
//        val validContent = "유효한 내용"
//
//        context("올바른 파라미터 요청 시") {
//
//            it("생성에 성공한다.") {
//                shouldNotThrowAny { PostUpdateCommand(title = validTitle, content = validContent) }
//            }
//        }
//
//        context("유효하지 않은 파라미터 요청 시") {
//            val invalidTitle = listOf("", " ", "".padEnd(20))
//            val invalidContent = listOf("", " ", "".padEnd(300))
//
//            it("예외를 던진다.") {
//                invalidTitle.forEach {
//                    shouldThrow<BaseException> { PostUpdateCommand(title = it, content = validContent) }
//                        .apply { baseErrorCode.httpStatus shouldBe HttpStatus.BAD_REQUEST }
//                }
//                invalidContent.forEach {
//                    shouldThrow<BaseException> { PostUpdateCommand(title = validTitle, content = it) }
//                        .apply { baseErrorCode.httpStatus shouldBe HttpStatus.BAD_REQUEST }
//                }
//            }
//        }
//    }
//})
