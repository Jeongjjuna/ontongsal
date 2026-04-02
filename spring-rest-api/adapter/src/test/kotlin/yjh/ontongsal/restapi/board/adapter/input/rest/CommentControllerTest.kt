package yjh.ontongsal.restapi.board.adapter.input.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import yjh.ontongsal.restapi.usecase.CommentUseCase
import yjh.ontongsal.restapi.usecase.GetCommentUseCase
import yjh.ontongsal.restapi.web.CommentController

@DisplayName("[RestDocs] - 댓글")
@WebMvcTest(CommentController::class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class CommentControllerTest(
    @Autowired
    private val mockMvc: MockMvc,

    @MockkBean
    private val commentUseCase: CommentUseCase,

    @MockkBean
    private val getCommentUseCase: GetCommentUseCase,
) : DescribeSpec({

    val objectMapper = ObjectMapper()

    describe("POST : /v1/articles/{id}/comments") {
        val articleId = 999
        val body = mapOf("content" to "새로운 내용")

        context("특정 게시글에 댓글 생성요청을 하면") {
            it("200 SUCCESS") {
                mockMvc.perform(
                    post("/v1/articles/{id}/comments", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                )
                    .andDo(print())
                    .andExpect(status().isCreated)
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(0))
                    .andDo(
                        document(
                            "댓글 생성 요청",
                            requestFields(
                                fieldWithPath("content").description("댓글 내용")
                            ),
                            responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("details").optional().description("상세 데이터")
                            )
                        )
                    )
            }
        }
    }
})
