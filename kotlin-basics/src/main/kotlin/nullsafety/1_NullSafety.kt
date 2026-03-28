package nullsafety

class Name{
}

fun main() {
    // 1. 모든 프로퍼티에는 값을 할당해야 한다. 묵시적으로 null이 할당되지 않는다.
    class Name {
        // var name: String // 컴파일 에러
    }


    // 2. nullable 타입을 사용해야함
    var name: String? = null


    // 3. nullable 타입은, 직접 호출할 수 없다.
    // name.length   // 컴파일 에러
    name?.length  // 안전 호출 사용(추천o)
    name!!.length // 널아님 어설션 사용(추천x)


    // 4. 스마트 캐스팅 : 코틀린 컴파일러가 null이 아님을 알고 스마트하게 타입을 캐스팅 함.
    if (name != null) {
        name.length
    }

    if(name == null) return
    name.length

    if (name == null) throw Error()
    name.length

    if (name != null && name.length > 0) { }

    if (name == null || name.length == 0) { }

    requireNotNull(name)
    println(name.length)


    // 5. 엘비스 연산자
    name ?: "new name"


    // 6. nullable 타입의 확장함수 : 코틀린 표준 라이브러리의 유용한 함수
    name.orEmpty()       // null이면 빈문자열 반환
    name.isNullOrEmpty() // null이거나 비어있는 문자열 이면 true반환
    name.isNullOrBlank() // null이거나 공백이면 true 반환
    // 그 외에 널 가능 리스트용 유용한 함수도 많이 있음
}