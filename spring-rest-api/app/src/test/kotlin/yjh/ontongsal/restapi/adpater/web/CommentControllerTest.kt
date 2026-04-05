package yjh.ontongsal.restapi.adpater.web

import com.epages.restdocs.apispec.ResourceDocumentation.headerWithName
import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import yjh.ontongsal.restapi.application.usecase.CommentUseCase
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.AuditInfo
import yjh.ontongsal.restapi.domain.Comment
import yjh.ontongsal.restapi.domain.CommentContent
import yjh.ontongsal.restapi.domain.CommentStatus
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Slice
import java.time.Instant

@DisplayName("[RestDocs] - 댓글")
@WebMvcTest(CommentController::class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class CommentControllerTest(
    private val mockMvc: MockMvc,

    @MockkBean
    private val commentUseCase: CommentUseCase,
) : DescribeSpec({

    val objectMapper = ObjectMapper()

    val fixedNow = Instant.parse("2024-01-01T00:00:00Z")
    val stubComment = Comment(
        id = 1L,
        articleId = 999L,
        authorId = 100L,
        auditInfo = AuditInfo(createdAt = fixedNow, updatedAt = fixedNow),
        content = CommentContent("test content"),
        status = CommentStatus.ACTIVE,
    )

    describe("POST : /v1/articles/{id}/comments") {
        context("특정 게시글에 댓글 생성 요청을 하면") {
            every { commentUseCase.write(any<Actor>(), any<TargetId>(), any<CommentContent>()) } returns 1L

            it("201 Created - Location 헤더와 함께 생성 성공") {
                val body = mapOf("content" to "test content")

                mockMvc.perform(
                    post("/v1/articles/{id}/comments", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                )
                    .andDo(print())
                    .andExpect(status().isCreated)
                    .andExpect(header().exists("Location"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(0))
                    .andDo(
                        document(
                            "comment-write",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("댓글")
                                    .summary("댓글 생성")
                                    .description("특정 게시글에 새로운 댓글을 작성합니다.")
                                    .pathParameters(
                                        parameterWithName("id").description("게시글 ID")
                                    )
                                    .requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용 (1자 이상, 5000자 이하)")
                                    )
                                    .responseHeaders(
                                        headerWithName("Location").description("생성된 댓글의 URI")
                                    )
                                    .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (0: 성공)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("details").type(JsonFieldType.VARIES).optional().description("상세 데이터"),
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }

    describe("PUT : /v1/comments/{id}") {
        context("댓글 수정 요청을 하면") {
            every { commentUseCase.update(any<Actor>(), any<TargetId>(), any<CommentContent>()) } returns 1L

            it("201 Created - Location 헤더와 함께 수정 성공") {
                val body = mapOf("content" to "updated content")

                mockMvc.perform(
                    put("/v1/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                )
                    .andDo(print())
                    .andExpect(status().isCreated)
                    .andExpect(header().exists("Location"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(0))
                    .andDo(
                        document(
                            "comment-update",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("댓글")
                                    .summary("댓글 수정")
                                    .description("기존 댓글의 내용을 수정합니다.")
                                    .pathParameters(
                                        parameterWithName("id").description("댓글 ID")
                                    )
                                    .requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 댓글 내용 (1자 이상, 5000자 이하)")
                                    )
                                    .responseHeaders(
                                        headerWithName("Location").description("수정된 댓글의 URI")
                                    )
                                    .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (0: 성공)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("details").type(JsonFieldType.VARIES).optional().description("상세 데이터"),
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }

    describe("DELETE : /v1/comments/{id}") {
        context("댓글 삭제 요청을 하면") {
            every { commentUseCase.delete(any<Actor>(), any<TargetId>()) } just Runs

            it("204 No Content - 삭제 성공") {
                mockMvc.perform(delete("/v1/comments/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isNoContent)
                    .andDo(
                        document(
                            "comment-delete",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("댓글")
                                    .summary("댓글 삭제")
                                    .description("댓글을 삭제합니다. (논리 삭제)")
                                    .pathParameters(
                                        parameterWithName("id").description("댓글 ID")
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }

    describe("GET : /v1/comments") {
        context("게시글의 댓글 목록 조회 요청을 하면") {
            every {
                commentUseCase.findComments(any<TargetId>(), any<Int>(), any<Int>())
            } returns Slice(listOf(stubComment), hasNext = false)

            it("200 OK - 댓글 목록과 다음 페이지 여부를 반환한다") {
                mockMvc.perform(
                    get("/v1/comments")
                        .param("articleId", "999")
                        .param("offset", "0")
                        .param("limit", "10")
                )
                    .andDo(print())
                    .andExpect(status().isOk)
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.details.content[0].id").value(1))
                    .andExpect(jsonPath("$.details.content[0].articleId").value(999))
                    .andExpect(jsonPath("$.details.hasNext").value(false))
                    .andDo(
                        document(
                            "comment-list",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("댓글")
                                    .summary("댓글 목록 조회")
                                    .description("특정 게시글의 댓글 목록을 오프셋 페이징으로 조회합니다.")
                                    .queryParameters(
                                        parameterWithName("articleId").description("게시글 ID"),
                                        parameterWithName("offset").description("조회 시작 오프셋 (0부터 시작)"),
                                        parameterWithName("limit").description("한 번에 조회할 댓글 수"),
                                    )
                                    .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (0: 성공)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("details").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("details.content[]").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                        fieldWithPath("details.content[].id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                        fieldWithPath("details.content[].articleId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        fieldWithPath("details.content[].authorId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                        fieldWithPath("details.content[].createdAt").type(JsonFieldType.STRING).description("작성일시 (ISO 8601)"),
                                        fieldWithPath("details.content[].updatedAt").type(JsonFieldType.STRING).optional().description("수정일시 (ISO 8601)"),
                                        fieldWithPath("details.content[].content").type(JsonFieldType.OBJECT).description("댓글 내용 객체"),
                                        fieldWithPath("details.content[].content.contentText").type(JsonFieldType.STRING).description("댓글 내용 텍스트"),
                                        fieldWithPath("details.content[].status").type(JsonFieldType.STRING).description("댓글 상태 (ACTIVE / DELETED)"),
                                        fieldWithPath("details.hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }
})
