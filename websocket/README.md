## Spring Boot + Websocket


### 기본적인 WebSocket 사용하기
- 의존성을 추가한다.
    ```
    "org.springframework.boot:spring-boot-starter-websocket"
    ```
- @EnableWebSocket 사용
- WebSocketHandlerRegistry 타입의 registry에 웹소켓 핸들러를 등록해야한다.
  - 웹소켓 핸들러는 연결을 수립하고, 어떻게 처리할지를 정의한다.
  - 제공되는 몇몇 클래스를 상속해서 재정의한다.(개발자가 프로그래밍)
  - 즉, 시큐리티의 필터를 정의하고 등록해주는 것 처럼 핸들르러를 정의하고 등록해주면 된다.
- 연결된 세션을 관리해주기 위해 ConcurrentHashMap 활용
  - 동시에 많은 세션에서 접할 수 있기때문에, 스레드 안전한 자료구조를 활용한다.
