## Spring Boot + Kotlin



- Ktlint 사용 : 빠르고 반복적인 실행, 코드 일관성(컨벤션/스타일)
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

- 코틀린에서 setter를 닫고, getter를 열고 싶음
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
