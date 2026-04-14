package yjh.ontongsal.restapi.adapter.web

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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import yjh.ontongsal.restapi.application.usecase.BoardUseCase
import yjh.ontongsal.restapi.domain.*
import java.time.Instant

@DisplayName("[RestDocs] - 게시판")
@WebMvcTest(BoardController::class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class BoardControllerTest(
    private val mockMvc: MockMvc,

    @MockkBean
    private val boardUseCase: BoardUseCase,
) : DescribeSpec({

    val objectMapper = ObjectMapper()

    val fixedNow = Instant.parse("2024-01-01T00:00:00Z")
    val stubBoard = Board(
        id = 1L,
        managerId = 100L,
        name = BoardName("테스트 게시판"),
        status = BoardStatus.ACTIVE,
        auditInfo = AuditInfo(createdAt = fixedNow, createdBy = "관리자", updatedAt = fixedNow, updatedBy = "관리자"),
    )

    describe("POST : /v1/admin/boards") {
        context("게시판 생성 요청을 하면") {
            every { boardUseCase.create(any<Actor>(), any<BoardName>()) } returns 1L

            it("201 Created - Location 헤더와 함께 생성 성공") {
                val body = mapOf("name" to "테스트 게시판")

                mockMvc.perform(
                    post("/v1/admin/boards")
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
                            "board-create",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시판")
                                    .summary("게시판 생성")
                                    .description("새로운 게시판을 생성합니다. (어드민 전용)")
                                    .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("게시판 이름 (1자 이상, 50자 이하)")
                                    )
                                    .responseHeaders(
                                        headerWithName("Location").description("생성된 게시판의 URI")
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

    describe("PUT : /v1/admin/boards/{id}") {
        context("게시판 수정 요청을 하면") {
            every { boardUseCase.update(any<Actor>(), any<TargetId>(), any<BoardName>()) } returns 1L

            it("201 Created - Location 헤더와 함께 수정 성공") {
                val body = mapOf("name" to "수정된 게시판")

                mockMvc.perform(
                    put("/v1/admin/boards/{id}", 1L)
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
                            "board-update",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시판")
                                    .summary("게시판 수정")
                                    .description("게시판 이름을 수정합니다. (어드민 전용)")
                                    .pathParameters(
                                        parameterWithName("id").description("게시판 ID")
                                    )
                                    .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 게시판 이름 (1자 이상, 50자 이하)")
                                    )
                                    .responseHeaders(
                                        headerWithName("Location").description("수정된 게시판의 URI")
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

    describe("DELETE : /v1/admin/boards/{id}") {
        context("게시판 삭제 요청을 하면") {
            every { boardUseCase.delete(any<Actor>(), any<TargetId>()) } just Runs

            it("204 No Content - 삭제 성공") {
                mockMvc.perform(delete("/v1/admin/boards/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isNoContent)
                    .andDo(
                        document(
                            "board-delete",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시판")
                                    .summary("게시판 삭제")
                                    .description("게시판을 삭제합니다. (논리 삭제, 어드민 전용)")
                                    .pathParameters(
                                        parameterWithName("id").description("게시판 ID")
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }

    describe("GET : /v1/admin/boards/{id}") {
        context("게시판 단건 조회 요청을 하면") {
            every { boardUseCase.findBoard(any<TargetId>()) } returns stubBoard

            it("200 OK - 게시판 상세 정보를 반환한다") {
                mockMvc.perform(get("/v1/admin/boards/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk)
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.details.id").value(1))
                    .andExpect(jsonPath("$.details.name").value("테스트 게시판"))
                    .andDo(
                        document(
                            "board-get",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시판")
                                    .summary("게시판 단건 조회")
                                    .description("게시판 ID로 게시판 상세 정보를 조회합니다. (어드민 전용)")
                                    .pathParameters(
                                        parameterWithName("id").description("게시판 ID")
                                    )
                                    .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (0: 성공)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("details").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("details.id").type(JsonFieldType.NUMBER).description("게시판 ID"),
                                        fieldWithPath("details.managerId").type(JsonFieldType.NUMBER).description("관리자 사용자 ID"),
                                        fieldWithPath("details.name").type(JsonFieldType.STRING).description("게시판 이름"),
                                        fieldWithPath("details.status").type(JsonFieldType.STRING).description("게시판 상태 (ACTIVE / DELETED)"),
                                        fieldWithPath("details.createdAt").type(JsonFieldType.STRING).description("생성일시 (ISO 8601)"),
                                        fieldWithPath("details.updatedAt").type(JsonFieldType.STRING).description("수정일시 (ISO 8601)"),
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }

    describe("GET : /v1/admin/boards") {
        context("게시판 목록 조회 요청을 하면") {
            every { boardUseCase.findAll() } returns listOf(stubBoard)

            it("200 OK - 게시판 목록을 반환한다") {
                mockMvc.perform(get("/v1/admin/boards"))
                    .andDo(print())
                    .andExpect(status().isOk)
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.details[0].id").value(1))
                    .andExpect(jsonPath("$.details[0].name").value("테스트 게시판"))
                    .andDo(
                        document(
                            "board-list",
                            resource(
                                ResourceSnippetParameters.builder()
                                    .tags("게시판")
                                    .summary("게시판 목록 조회")
                                    .description("전체 게시판 목록을 조회합니다. (어드민 전용)")
                                    .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (0: 성공)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("details[]").type(JsonFieldType.ARRAY).description("게시판 목록"),
                                        fieldWithPath("details[].id").type(JsonFieldType.NUMBER).description("게시판 ID"),
                                        fieldWithPath("details[].managerId").type(JsonFieldType.NUMBER).description("관리자 사용자 ID"),
                                        fieldWithPath("details[].name").type(JsonFieldType.STRING).description("게시판 이름"),
                                        fieldWithPath("details[].status").type(JsonFieldType.STRING).description("게시판 상태 (ACTIVE / DELETED)"),
                                        fieldWithPath("details[].createdAt").type(JsonFieldType.STRING).description("생성일시 (ISO 8601)"),
                                        fieldWithPath("details[].updatedAt").type(JsonFieldType.STRING).description("수정일시 (ISO 8601)"),
                                    )
                                    .build()
                            )
                        )
                    )
            }
        }
    }
})
