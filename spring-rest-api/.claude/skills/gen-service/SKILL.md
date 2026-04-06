---
name: gen-service
description: UseCase 인터페이스 파일을 입력받아 Command/Query Service, Creator/Updater/Deleter/Finder/EventHandler 컴포넌트, Repository 포트, 도메인 이벤트를 생성합니다.
argument-hint: "<UseCase 파일 경로>"
allowed-tools: Read, Write, Glob, Grep
---

## 역할

아래 프로젝트 패턴을 완전히 이해하고, 입력된 UseCase 파일을 분석하여 **application 레이어의 비즈니스 로직 구현체 전체**를 생성하라.

---

## 입력

UseCase 파일 경로: `$ARGUMENTS`

먼저 해당 파일을 Read 툴로 읽어라. 이후 **같은 디렉토리(usecase/)** 를 Glob으로 스캔하여 연관 UseCase(Get*, Find* 등)가 있는지 확인하라.

---

## 프로젝트 모듈 구조

```
domain        ← 순수 도메인 (Aggregate, ValueObject, Event, ErrorCode)
application   ← UseCase 인터페이스 + Service 구현 + port 인터페이스
  ├── usecase/          ← 인터페이스 (명령: XxxUseCase, 조회: GetXxxUseCase)
  ├── port/             ← XxxRepository, TransactionRunner, EventPublisher, CircuitBreakerRunner
  └── (root)            ← XxxCommandService, XxxQueryService, XxxCreator, XxxUpdater, ...
adapter       ← JPA/Web 구현체
app           ← 부트스트랩
```

**생성 대상 위치**: `application/src/main/kotlin/yjh/ontongsal/restapi/application/`

---

## 레퍼런스 패턴 (Comment 도메인)

### CommentUseCase (명령 UseCase)

```kotlin
interface CommentUseCase {
    fun write(actor: Actor, articleId: TargetId, commentContent: CommentContent): Long
    fun update(actor: Actor, commentId: TargetId, commentContent: CommentContent): Long
    fun delete(actor: Actor, commentId: TargetId)
    fun findComments(articleId: TargetId, offset: Int, limit: Int): Slice<Comment>
}
```

### CommentRepository (포트 인터페이스)

```kotlin
interface CommentRepository {
    fun save(actor: Actor, article: Article, commentContent: CommentContent): Comment
    fun update(comment: Comment): Comment
    fun delete(comment: Comment)
    fun findById(targetId: TargetId): Comment?
    fun findAllBy(articleId: Long, commentIdCursor: Long, size: Int): List<Comment>
    fun findAll(articleId: TargetId, offset: Int, limit: Int): Slice<Comment>
}
```

### CommentService

```kotlin
@Service
class CommentService(
    private val commentCreator: CommentCreator,
    private val commentUpdater: CommentUpdater,
    private val commentDeleter: CommentDeleter,
    private val commentFinder: CommentFinder,
    private val commentEventHandler: CommentEventHandler,
) : CommentUseCase {

    override fun write(actor: Actor, articleId: TargetId, commentContent: CommentContent): Long {
        val createdComment = commentCreator.create(actor, articleId, commentContent)
        commentEventHandler.created(actor, articleId, createdComment)
        return createdComment.id
    }

    override fun update(actor: Actor, commentId: TargetId, commentContent: CommentContent): Long {
        val updatedComment = commentUpdater.update(actor, commentId, commentContent)
        commentEventHandler.updated(actor, commentId, updatedComment)
        return updatedComment.id
    }

    override fun delete(actor: Actor, commentId: TargetId) {
        commentDeleter.delete(actor, commentId)
    }

    override fun findComments(articleId: TargetId, offset: Int, limit: Int): Slice<Comment> {
        return commentFinder.find(articleId, offset, limit)
    }
}
```

### CommentCreator

```kotlin
@Component
class CommentCreator(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val transaction: TransactionRunner,
) {
    fun create(actor: Actor, targetId: TargetId, commentContent: CommentContent): Comment {
        return transaction.run {
            val article = articleRepository.findById(targetId)
                ?: throw AppException.NotFound(ErrorCode.ARTICLE_NOT_FOUND)
            commentRepository.save(actor, article, commentContent)
        }.also {
            logger.info { "comment created : commentId=${it.id}, articleId=$targetId" }
        }
    }
}
```

### CommentUpdater

```kotlin
@Component
class CommentUpdater(
    private val commentRepository: CommentRepository,
    private val transaction: TransactionRunner,
) {
    fun update(actor: Actor, commentId: TargetId, commentContent: CommentContent): Comment {
        return transaction.run {
            val comment = commentRepository.findById(commentId)
                ?: throw AppException.NotFound(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다. [$commentId]")
            comment.update(actor, commentContent)
            commentRepository.update(comment)
        }.also {
            logger.info { "comment modified : commentId=$commentId" }
        }
    }
}
```

### CommentDeleter

```kotlin
@Component
class CommentDeleter(
    private val commentRepository: CommentRepository,
    private val transaction: TransactionRunner,
) {
    fun delete(actor: Actor, commentId: TargetId) {
        transaction.run {
            val comment = commentRepository.findById(commentId)
                ?: throw AppException.NotFound(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다. [$commentId]")
            comment.delete(actor)
            commentRepository.delete(comment)
        }.also {
            logger.info { "comment deleted : commentId=$commentId" }
        }
    }
}
```

### CommentFinder

```kotlin
@Component
class CommentFinder(
    private val commentRepository: CommentRepository,
) {
    fun find(articleId: TargetId, offset: Int, limit: Int): Slice<Comment> {
        return commentRepository.findAll(articleId, offset, limit)
    }
}
```

### CommentEventHandler

```kotlin
@Component
class CommentEventHandler(
    private val eventPublisher: EventPublisher,
) {
    fun created(actor: Actor, articleId: TargetId, comment: Comment) {
        eventPublisher.publish(CommentCreatedEvent.of(actor, articleId, comment))
        logger.debug { "comment created event published : commentId=${comment.id}" }
    }

    fun updated(actor: Actor, commentId: TargetId, comment: Comment) {
        eventPublisher.publish(CommentModifiedEvent.of(actor, commentId, comment))
        logger.debug { "comment modified event published : commentId=${comment.id}" }
    }
}
```

---

## 생성 규칙

### 1. 도메인 분석

UseCase 파일을 읽고 다음을 파악하라:
- **도메인명** (`Comment`, `Article` 등)
- **명령 메서드** (`write`, `update`, `delete` 포함 여부)
- **조회 메서드** (`find*`, `get*` 포함 여부)
- **반환 타입** (단건 도메인, `Slice<T>`, `List<T>`, `Long` 등)
- **파라미터 타입** (`Actor`, `TargetId`, ValueObject 등)

이후 `domain/` 모듈 하위를 Glob으로 스캔하여 해당 도메인의 **Aggregate**, **ValueObject**, **ErrorCode**, **도메인 이벤트** 파일이 있는지 확인하라.

### 2. 생성할 파일 결정

| 조건 | 생성 파일 |
|------|-----------|
| `write` / `create` 메서드 있음 | `{Domain}Creator.kt` |
| `update` / `modify` 메서드 있음 | `{Domain}Updater.kt` |
| `delete` 메서드 있음 | `{Domain}Deleter.kt` |
| `find*` / `get*` 메서드 있음 | `{Domain}Finder.kt` |
| 명령 UseCase 존재 | `{Domain}CommandService.kt` |
| 별도 조회 UseCase(`Get{Domain}UseCase`) 존재 | `{Domain}QueryService.kt` (CircuitBreaker 적용) |
| 명령/이벤트 조합 있음 | `{Domain}EventHandler.kt` |
| Repository 포트 미존재 | `port/{Domain}Repository.kt` |
| CircuitBreakerNames에 해당 도메인 상수 없음 | `CircuitBreakerNames.kt` 수정 |

### 3. CommandService 규칙

- 클래스명: `{Domain}CommandService`
- `@Service` 어노테이션
- 생성자에 Creator/Updater/Deleter/Finder/EventHandler 주입
- 각 UseCase 메서드를 해당 컴포넌트에 **위임(delegation)** 만 함 — 비즈니스 로직 직접 작성 금지
- 이벤트 발행이 필요한 명령(write/update)은 컴포넌트 호출 후 EventHandler 호출

### 4. QueryService 규칙 (별도 조회 UseCase가 있을 때만)

- 클래스명: `{Domain}QueryService`
- `@Service` 어노테이션
- `CircuitBreakerRunner` 주입 — 조회 실패 시 fallback으로 빈 컬렉션 또는 예외 반환
- `CircuitBreakerNames`에 `{DOMAIN}_READ` 상수 추가

### 5. Creator / Updater / Deleter 규칙

- `@Component` 어노테이션
- `TransactionRunner` 주입 — **반드시 `transaction.run { }` 블록 내에서** DB 접근
- 조회 실패 시 `AppException.NotFound(ErrorCode.{DOMAIN}_NOT_FOUND, "...")` throw
- 도메인 객체의 메서드(`update()`, `delete()` 등) 호출로 상태 변경 — 직접 필드 조작 금지
- `.also { logger.info { "..." } }` 로 로그 기록

### 6. Finder 규칙

- `@Component` 어노테이션
- `TransactionRunner` 불필요 (읽기 전용)
- Repository 포트에 위임만 함

### 7. EventHandler 규칙

- `@Component` 어노테이션
- `EventPublisher` 주입
- 메서드명: `created(...)`, `updated(...)`, `deleted(...)`
- `DomainEvent.of(...)` factory 메서드 호출 후 `eventPublisher.publish(...)` 실행
- `logger.debug { }` 로 이벤트 발행 로그

### 8. Repository 포트 규칙

- 위치: `application/src/main/kotlin/yjh/ontongsal/restapi/application/port/`
- 명령 메서드: `save(...)`, `update(...)`, `delete(...)`
- 조회 메서드: `findById(targetId: TargetId)`, `findAll(...)`, `findAllBy(...)`
- 반환 타입: 단건은 `Domain?`, 목록은 `Slice<Domain>` 또는 `List<Domain>`

### 9. 파일 존재 확인 필수

각 파일을 생성하기 전에 **Glob**으로 해당 경로에 파일이 이미 존재하는지 확인하라. 이미 존재하면 **Read** 후 누락된 메서드/기능만 추가하고, 기존 코드는 건드리지 않는다.

---

## 생성 절차

1. `$ARGUMENTS` 경로의 UseCase 파일을 **Read**로 읽는다
2. `usecase/` 디렉토리를 **Glob**으로 스캔하여 연관 UseCase 파일 확인
3. `domain/` 모듈을 **Glob**으로 스캔하여 Aggregate, ErrorCode, 도메인 이벤트 파일 확인
4. `application/port/` 디렉토리를 **Glob**으로 스캔하여 Repository 포트 존재 여부 확인
5. `application/` 루트를 **Glob**으로 스캔하여 기존 Creator/Updater 등 존재 여부 확인
6. 위 분석을 바탕으로 생성/수정할 파일 목록을 결정하고 순서대로 **Write**
   - 생성 순서: Repository 포트 → Creator → Updater → Deleter → Finder → EventHandler → CommandService → QueryService → CircuitBreakerNames
7. 생성한 파일 목록과 각 파일의 역할을 요약하여 사용자에게 보고한다
