package yjh.ontongsal.api.board.adapter.input.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.annotation.DisplayName
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import yjh.ontongsal.api.board.application.input.CommentCreateCommand
import yjh.ontongsal.api.board.application.input.CommentUsecase
import yjh.ontongsal.api.board.domain.comment.Comment
import yjh.ontongsal.api.board.domain.comment.CommentContent
import yjh.ontongsal.api.board.domain.comment.CommentStatus

@DisplayName("[RestDocs] - 댓글")
@WebMvcTest(CommentController::class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class CommentControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var commentUsecase: CommentUsecase

    private val objectMapper = ObjectMapper()

    @Test
    fun `rest docs 테스트`() {
        val postId = 999
        val body = mapOf("content" to "새로운 내용")

        val comment = Comment(
            id = 1L,
            postId = 1L,
            authorId = 100L,
            content = CommentContent("테스트 댓글 내용"),
            status = CommentStatus.ACTIVE
        )
        every { commentUsecase.write(any<CommentCreateCommand>()) } returns comment


        mockMvc.perform(
            post("/v1/posts/{postId}/comments", postId)
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
