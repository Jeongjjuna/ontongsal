---
name: gen-controller
description: UseCase 인터페이스 파일을 입력받아 Controller와 Spring REST Docs 기반 ControllerTest를 생성합니다.
argument-hint: "<UseCase 파일 경로>"
allowed-tools: Read, Write, Glob, Grep
---

## 역할

아래 프로젝트 패턴을 완전히 이해하고, 입력된 UseCase 파일을 분석하여 **Controller**와 **ControllerTest**를 생성하라.

---

## 입력

UseCase 파일 경로: `$ARGUMENTS`

먼저 해당 파일을 Read 툴로 읽어라.

---

## 프로젝트 구조

```
domain        ← 순수 도메인 (Spring 의존성 없음)
application   ← UseCase/Service 인터페이스
adapter       ← Web + JPA 구현체
app           ← 부트스트랩 (실행 진입점, 테스트 위치)
```

- Controller 위치: `adapter/src/main/kotlin/yjh/ontongsal/restapi/adapter/web/`
- DTO 위치: `adapter/src/main/kotlin/yjh/ontongsal/restapi/adapter/web/dto/`
- ControllerTest 위치: `app/src/test/kotlin/yjh/ontongsal/restapi/adapter/web/`

---

## 레퍼런스 패턴

### CommentUseCase (레퍼런스 UseCase)

```kotlin
interface CommentUseCase {
    fun write(actor: Actor, articleId: TargetId, commentContent: CommentContent): Long
    fun update(actor: Actor, commentId: TargetId, commentContent: CommentContent): Long
    fun delete(actor: Actor, commentId: TargetId)
    fun findComments(articleId: TargetId, offset: Int, limit: Int): Slice<Comment>
}
```

### CommentController (레퍼런스 Controller)

```kotlin
@RestController
class CommentController(
    private val commentUseCase: CommentUseCase,
) {

    @PostMapping("/v1/articles/{id}/comments")
    fun createComment(
        actor: Actor,
        @PathVariable id: Long,
        @Valid @RequestBody request: CommentCreateRequest,
    ): ResponseEntity<Unit> {
        val successId = commentUseCase.write(actor, TargetId(id), request.toContent())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @PutMapping("/v1/comments/{id}")
    fun modifyComment(
        actor: Actor,
        @PathVariable id: Long,
        @Valid @RequestBody request: CommentUpdateRequest,
    ): ResponseEntity<Unit> {
        val successId = commentUseCase.update(actor, TargetId(id), request.toContent())
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @DeleteMapping("/v1/comments/{id}")
    fun deleteComment(
        actor: Actor,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        commentUseCase.delete(actor, TargetId(id))
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/v1/comments")
    fun getComments(
        @RequestParam articleId: Long,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ResponseEntity<SliceResponse<CommentResponse>> {
        val slice = commentUseCase.findComments(TargetId(articleId), offset, limit)
        return ResponseEntity.ok(SliceResponse(CommentResponse.of(slice.content), slice.hasNext))
    }
}
```

### CommentControllerTest (레퍼런스 Test)

```kotlin
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
        auditInfo = AuditInfo(createdAt = fixedNow, createdBy = "홍길동", updatedAt = fixedNow, updatedBy = "홍길동"),
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
                                    .pathParameters(parameterWithName("id").description("게시글 ID"))
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
                                    .pathParameters(parameterWithName("id").description("댓글 ID"))
                                    .build()
                            )
                        )
                    )
            }
        }
    }

    describe("GET : /v1/comments") {
        context("댓글 목록 조회 요청을 하면") {
            every { commentUseCase.findComments(any<TargetId>(), any<Int>(), any<Int>()) } returns Slice(listOf(stubComment), hasNext = false)

            it("200 OK - 목록과 hasNext 반환") {
                mockMvc.perform(
                    get("/v1/comments")
                        .param("articleId", "999")
                        .param("offset", "0")
                        .param("limit", "10")
                )
                    .andDo(print())
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.details.content[0].id").value(1))
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
                                        fieldWithPath("details.content[]").type(JsonFieldType.ARRAY).description("목록"),
                                        fieldWithPath("details.content[].id").type(JsonFieldType.NUMBER).description("댓글 ID"),
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
```

---

## 생성 규칙

### Controller 규칙

1. **클래스명**: `{Domain}Controller`
2. **패키지**: `yjh.ontongsal.restapi.adapter.web`
3. **`@RestController`** 어노테이션 사용
4. UseCase를 생성자 주입
5. **HTTP 메서드 매핑**:
   - `write` / `create` → `@PostMapping` → 201 Created + Location 헤더 반환
   - `update` / `modify` → `@PutMapping("/{id}")` → 201 Created + Location 헤더 반환
   - `delete` → `@DeleteMapping("/{id}")` → 204 No Content
   - `find` (단건) → `@GetMapping("/{id}")` → 200 OK + Response body
   - `findAll` / `finds` (목록) → `@GetMapping` + `@RequestParam` → 200 OK + `SliceResponse<XxxResponse>`
6. **`Actor`** 파라미터: 인증이 필요한 명령(write/update/delete)에 첫 번째 파라미터로 추가 (어노테이션 없음 — ArgumentResolver로 주입됨)
7. **`TargetId`**: PathVariable Long을 `TargetId(id)`로 감싸서 UseCase에 전달
8. 요청 DTO는 `@Valid @RequestBody`로 받고, `.toXxx()` 변환 메서드 사용
9. URL 경로 패턴: `/v1/{복수형-소문자}` (예: `/v1/articles`, `/v1/comments`)

### Request/Response DTO 규칙

- `Write{Domain}Request` / `Create{Domain}Request`: 생성 요청 DTO
- `Update{Domain}Request`: 수정 요청 DTO
- `{Domain}Response`: 응답 DTO — `companion object { fun of(domain): XxxResponse }` 포함
- 각 DTO에는 `@field:NotNull`, `@field:NotBlank` validation 추가
- 도메인 변환 메서드 (`toXxx()`) 포함

**단일 책임 원칙**: DTO는 하나의 엔드포인트에서만 사용한다. 여러 엔드포인트가 비슷한 필드를 공유하더라도 DTO를 재사용하지 않는다. 예를 들어 생성과 수정이 같은 필드를 받더라도 `Write{Domain}Request`와 `Update{Domain}Request`를 각각 별도로 만든다. Response도 마찬가지로, 목록 조회와 단건 조회가 다른 필드를 포함할 경우 `{Domain}Response`와 `{Domain}DetailResponse`처럼 분리한다.

이미 존재하는 DTO 파일은 **Glob으로 확인** 후 신규 생성 여부 판단.

### ControllerTest 규칙

1. **클래스명**: `{Domain}ControllerTest`
2. **위치**: `app/src/test/kotlin/yjh/ontongsal/restapi/adapter/web/`
3. **어노테이션**: `@DisplayName("[RestDocs] - {도메인 한글명}")`, `@WebMvcTest({Domain}Controller::class)`, `@AutoConfigureRestDocs(outputDir = "build/generated-snippets")`
4. `DescribeSpec` 상속 (Kotest)
5. `@MockkBean`으로 UseCase Mock 주입
6. **각 엔드포인트마다 하나의 `describe` 블록**: 메서드 + 경로를 제목으로 (예: `"POST : /v1/articles"`)
7. **`every { ... }` Mock 설정**: `any<Type>()` 형태 사용
8. **REST Docs `document`** 이름: `{domain-name}-{action}` (예: `"article-write"`, `"comment-delete"`)
9. **`ResourceSnippetParameters.builder()`** 체인으로 tags, summary, description, 파라미터, requestFields, responseFields 기술
10. 응답 JSON 구조: `$.code`, `$.message`, `$.details` (프로젝트 공통 응답 래퍼)
11. stub 객체는 `val stub{Domain} = {Domain}(...)` 형태로 `fixedNow`와 함께 테스트 최상단에 선언

---

## 생성 절차

1. `$ARGUMENTS` 경로의 UseCase 파일을 **Read** 툴로 읽는다
2. UseCase의 메서드 시그니처를 분석하여 필요한 HTTP 엔드포인트를 파악한다
3. `adapter/src/main/kotlin/yjh/ontongsal/restapi/adapter/web/dto/` 경로에서 **Glob**으로 기존 DTO 파일 확인
4. 존재하지 않는 DTO가 있으면 생성한다
5. Controller 파일을 `adapter/src/main/kotlin/yjh/ontongsal/restapi/adapter/web/` 에 **Write**로 생성한다
6. ControllerTest 파일을 `app/src/test/kotlin/yjh/ontongsal/restapi/adapter/web/` 에 **Write**로 생성한다
7. 생성한 파일 목록과 각 엔드포인트를 요약하여 사용자에게 보고한다
