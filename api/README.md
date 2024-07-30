## Spring Boot + Kotlin



### Ktlint 사용 : 빠르고 반복적인 실행, 코드 일관성(컨벤션/스타일)
    ```
      id("org.jlleitschuh.gradle.ktlint") version "11.4.0"
    ```
  - 위 구문을 build.gradle.kts 플러그인에 추가한다.
  - Intellij의 Grade -> Tasks -> formatting -> ktlintFormat 실행한다.
  - 구체적인 formatting 설정은 .editorconfig 파일에 정의한다.
    ⚠️ 실제로 .editorconfig 파일에서 주석은 제거 해야함(주석이 있으면 인식을 못함)
    <details>
    <summary>주석 없는 .editorconfig 파일 보기</summary>

    ```
    root = true

    [*]
    charset = utf-8
    end_of_line = lf
    indent_style = space
    indent_size = 4
    insert_final_newline = true
    max_line_length = 120
    trim_trailing_whitespace = true
    no-wildcard-imports = enabled

    [{*.{kt, kts}}]
    ij_kotlin_allow_trailing_comma = true
    ```
    ---
    </details>

    ```
    root = true                             # 이 파일이 최상위에 존재하는 editorconfig 파일임을 명시(하위에 있더라도 덮어씀)

    [*]                                     # 모든 파일에 적용
    charset = utf-8                         # 파일 인코딩 UTF-8
    end_of_line = lf                        # 줄 끝을 LF(Line Feed)로 설정
    indent_style = space                    # 들여 쓰기를 공백(Space)로 설정
    indent_size = 4                         # 들여 쓰기 사이즈 4
    insert_final_newline = true             # 파일 끝에 새 줄을 추가
    max_line_length = 120                   # 한 줄의 최대 길이를 120 문자로 제한
    trim_trailing_whitespace = true         # 줄 끝의 공백 제거
    no-wildcard-imports = enabled           # 와일드카드 임포트 사용 하지 않음

    [{*.{kt, kts}}]                         # 모든 파일 중 확장자가 kt & kts 인 파일에만 적용
    ij_kotlin_allow_trailing_comma = true   # 인자 마지막 줄에 콤마(,) 생성
    ```



---

### 코틀린에서 setter를 닫고, getter를 열고 싶음
  1. backing property 사용(getter를 제공하는 방식)
      ```kotlin
      class Car(
        private var name: String
      ) {
        val name: String
            get() = this.name
      }
      ```
  2. privated set 사용(setter를 막아버리는 방식)
      ```kotlin
      class Car(
        name: String
      ) {
        var name = name
            private set
      }
      ```
  - 하지만 위의 방법으로 하면 코드가 길어지고, 코틀린 문법 장점이 사라짐
  - 선택 : var를 생성자에 사용하여 코틀린 문법 장점을 취하고, setter 대신 내부 메서드를 이용하자.

---

### 테스트 DB
  - Kotest는 Junit과 비교하여 중복을 줄이고 다양한 테스트 스타일을 지원한다.
  - docker container를 활용하여 테스트용 DB를 직접 띄운다.
    - 테스트할 때 테스트용 DB가 구동되어 있어야함.
  - test container를 활용하여 테스트 할 때에만 도커 컨테이너로 DB를 띄운다.
    - 테스트마다 test container의 시작/종료가 반복 되면 테스트가 오래 걸릴 수 있다.
    (한번띄우고 재사용하도록 최적화 필요)

---

### 코틀린 활용해보기

- 널 안정성
    - 물음표(?) 활용
    ```kotlin
    val a: String? = null
    println(a?.length)
    ```
    - let 활용
    ```kotlin
    val name: String? // name이 nullable 할 때?
    name?.let {
        // null이 아니라면 실행할 코드
    }
    ```
    - 엘비스 연산자
    ```kotlin
    val a: String? = null
    val b = a?.length ?: 0 // 엘비스 연산자
    // 단언 연산자(!!)도 있는데, 이건 지양 하자.
    ```

- 예외 처리 : 코틀린 제공 함수 많음
  - IllegalArgumentException
    - require(value: Boolean)
    - require(value: T?)
  - IllegalStateException
    - check(value: Boolean)
    - checkNotNull(value: T?)


- 컬렉션
  - 코틀린 표준 라이브러리 기본 컬렉션 타입 List, Map, Set
  - 불변 컬렉션 / 가변 컬렉션


- data 클래스
  - equals(), hashCode()
  - toString()
  - componentN() functions
  - copy()
