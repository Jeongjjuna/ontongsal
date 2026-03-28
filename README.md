# ontongsal

Spring Boot & Kotlin 학습을 위한 모노레포입니다.

## 프로젝트 구조

| 프로젝트 | 설명 | 외부 인프라 |
|---|---|---|
| `spring-rest-api` | REST API 설계 및 구현 | MySQL |
| `spring-websocket` | WebSocket 통신 | Redis |
| `spring-http-client` | HTTP 클라이언트 활용 | - |
| `spring-testing` | 테스트 전략 및 패턴 | - |
| `kotlin-basics` | Kotlin 기초 문법 및 활용 | - |

## 공용 인프라 (docker/)

각 프로젝트에서 필요한 외부 인프라는 `docker/docker-compose.yaml`로 통합 관리합니다.

| 서비스 | 이미지 | 포트 | 사용 프로젝트 |
|---|---|---|---|
| MySQL | `mysql:8.4` | 3306 | `spring-rest-api` |
| Redis | `redis:8.0` | 6379 | `spring-websocket` |
| MongoDB | `mongo:5.0` | 27017 | - |

### 실행 방법

```bash
# docker/ 디렉토리에서 실행
cd docker

# 시작
docker-compose -p "common-infrastructure" up -d

# 종료
docker-compose -p "common-infrastructure" down
```
