package yjh.ontongsal.restapi.adpater.web

import com.epages.restdocs.apispec.ResourceDocumentation.headerWithName
import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
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
import yjh.ontongsal.restapi.application.usecase.ArticleUseCase
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Article
import yjh.ontongsal.restapi.domain.ArticleContent
import yjh.ontongsal.restapi.domain.ArticleTitle
import yjh.ontongsal.restapi.domain.AuditInfo
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Slice
import java.time.Instant

@DisplayName("[RestDocs] - 게시글")
@WebMvcTest(ArticleController::class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class ArticleControllerTest(
    private val mockMvc: MockMvc,

    @MockkBean
    private val articleUseCase: ArticleUseCase,
) : DescribeSpec({

    val objectMapper = ObjectMapper()

    val fixedNow = Instant.parse("2024-01-01T00:00:00Z")
    val stubArticle = Article(
        id = 1L,
        boardId = 10L,
        writerId = 100L,
        title = ArticleTitle("테스트 제목"),
        content = ArticleContent("테스트 내용"),
        auditInfo = AuditInfo(createdAt = fixedNow, createdBy = "홍길동", updatedAt = fixedNow, updatedBy = "홍길동"),
    )

    describe("POST : /v1/articles") {
        context("게시글 생성 요청을 하면") {
            every { articleUseCase.write(any<Actor>(), any<TargetId>(), any<ArticleTitle>(), any<ArticleContent>()) } returns 1L

            it("201 Created - Location 헤더와 함께 생성 성공") {
                val body = mapOf(
                    "boardId" to 10,
                    "title" to "테스트 제목",
                    "content" to "테스트 내용",
                )

                mockMvc.perform(
                    post("/v1/articles")
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
                            "article-write",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시글")
                                    .summary("게시글 생성")
                                    .description("새로운 게시글을 작성합니다.")
                                    .requestFields(
                                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목 (1자 이상, 100자 이하)"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용 (1자 이상, 5000자 이하)"),
                                    )
                                    .responseHeaders(
                                        headerWithName("Location").description("생성된 게시글의 URI"),
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

    describe("PUT : /v1/articles/{id}") {
        context("게시글 수정 요청을 하면") {
            every { articleUseCase.update(any<Actor>(), any<TargetId>(), any<ArticleTitle>(), any<ArticleContent>()) } returns 1L

            it("201 Created - Location 헤더와 함께 수정 성공") {
                val body = mapOf(
                    "title" to "수정된 제목",
                    "content" to "수정된 내용",
                )

                mockMvc.perform(
                    put("/v1/articles/{id}", 1L)
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
                            "article-update",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시글")
                                    .summary("게시글 수정")
                                    .description("기존 게시글의 제목과 내용을 수정합니다.")
                                    .pathParameters(
                                        parameterWithName("id").description("게시글 ID"),
                                    )
                                    .requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 제목 (1자 이상, 100자 이하)"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 내용 (1자 이상, 5000자 이하)"),
                                    )
                                    .responseHeaders(
                                        headerWithName("Location").description("수정된 게시글의 URI"),
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

    describe("DELETE : /v1/articles/{id}") {
        context("게시글 삭제 요청을 하면") {
            every { articleUseCase.delete(any<Actor>(), any<TargetId>()) } just Runs

            it("204 No Content - 삭제 성공") {
                mockMvc.perform(delete("/v1/articles/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isNoContent)
                    .andDo(
                        document(
                            "article-delete",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시글")
                                    .summary("게시글 삭제")
                                    .description("게시글을 삭제합니다. (논리 삭제)")
                                    .pathParameters(
                                        parameterWithName("id").description("게시글 ID"),
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }

    describe("GET : /v1/articles/{id}") {
        context("게시글 단건 조회 요청을 하면") {
            every { articleUseCase.findArticle(any<TargetId>()) } returns stubArticle

            it("200 OK - 게시글 상세 정보를 반환한다") {
                mockMvc.perform(get("/v1/articles/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk)
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.details.id").value(1))
                    .andExpect(jsonPath("$.details.boardId").value(10))
                    .andExpect(jsonPath("$.details.writerId").value(100))
                    .andDo(
                        document(
                            "article-get",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시글")
                                    .summary("게시글 단건 조회")
                                    .description("게시글 ID로 특정 게시글을 조회합니다.")
                                    .pathParameters(
                                        parameterWithName("id").description("게시글 ID"),
                                    )
                                    .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (0: 성공)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("details").type(JsonFieldType.OBJECT).description("게시글 상세 데이터"),
                                        fieldWithPath("details.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        fieldWithPath("details.boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                                        fieldWithPath("details.writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                        fieldWithPath("details.title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("details.content").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("details.createdAt").type(JsonFieldType.STRING).description("작성일시 (ISO 8601)"),
                                        fieldWithPath("details.createdBy").type(JsonFieldType.STRING).description("작성자명"),
                                        fieldWithPath("details.updatedAt").type(JsonFieldType.STRING).optional().description("수정일시 (ISO 8601)"),
                                        fieldWithPath("details.updatedBy").type(JsonFieldType.STRING).optional().description("수정자명"),
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }
})
