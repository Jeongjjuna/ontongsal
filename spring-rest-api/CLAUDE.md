# Spring REST API

## 모듈 구조

4개의 독립 Gradle 모듈 (Composite Build):

```
domain        ← 순수 도메인 (Spring 의존성 없음)
application   ← UseCase/Service (Spring Context만)
adapter       ← Web + JPA 기타 구현체 (ex spring, redis, mysql, kafka... )
app           ← 부트스트랩 (실행 진입점)
```

의존 방향: `app` → `adapter` → `application` → `domain`

## Comment 도메인 예시

### domain
- `Comment.kt` — Aggregate Root (상태 검사, 수정/삭제 도메인 로직)
- `CommentStatus.kt` — Value Object enum (ACTIVE / DELETED, soft delete)
- `CommentContent.kt` — Value Object (최대 5000자 검증)
- `event/CommentCreatedEvent.kt` — Domain Event

### application
- `usecase/CommentUseCase.kt` — 명령 UseCase 인터페이스
- `usecase/GetCommentUseCase.kt` — 조회 UseCase 인터페이스
- `port/CommentRepository.kt` — Repository 포트 인터페이스
- `CommentCommandService.kt` — 명령 서비스 구현 (Creator/Updater/Deleter 위임)
- `CommentQueryService.kt` — 조회 서비스 구현 (CircuitBreaker 적용)
- `CommentCreator/Updater/Deleter/Finder.kt` — 세분화된 헬퍼
- `CommentEventHandler.kt` — 도메인 이벤트 발행

### adapter
- `web/CommentController.kt` — REST 엔드포인트
- `web/dto/Comment*Request.kt` — 요청 DTO (→ domain 변환 메서드 포함)
- `web/dto/Comment*Response.kt` — 응답 DTO (domain → DTO 변환 포함)
- `persistence/jpa/CommentAdapter.kt` — CommentRepository 포트 구현체
- `persistence/jpa/entity/CommentEntity.kt` — JPA 엔티티 (↔ domain 변환)
- `persistence/jpa/repository/CommentJpaRepository.kt` — Spring Data JPA

## 핵심 패턴

| 패턴 | 적용 |
|------|------|
| Hexagonal Architecture | Port(interface) → Adapter(구현체) |
| CQRS | Command/Query 서비스 분리 |
| Soft Delete | `CommentStatus.DELETED` |
| Domain Events | `CommentCreatedEvent`, `CommentModifiedEvent` |
| Circuit Breaker | 조회 서비스에 Resilience4j 적용 |
| Value Object | `CommentContent`, `Actor`, `TargetId` |
