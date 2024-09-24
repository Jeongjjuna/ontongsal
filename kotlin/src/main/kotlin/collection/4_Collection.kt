package collection


fun main() {

    // 읽기(immutable)
    val a = listOf(1, 2, 3)
    val b = listOf<Int>()

    val c = setOf(1, 2, 3)
    val d = setOf<Int>()

    val e = mapOf("one" to 1, "two" to 2, "three" to 3)
    val f = mapOf<String, Int>()

    // 읽기/쓰기(mutable)
    val g = mutableListOf(1, 2, 3)
    val h = mutableListOf<Int>()

    val i = mutableSetOf(1, 2, 3)
    val j = mutableSetOf<Int>()

    val k = mutableMapOf("one" to 1)
    val l = mutableMapOf<String, Int>()

    // 컬렉션 빌더는 내부에서 mutable 반환은 immutable
    val m: List<Int> = buildList {
        add(1)
        add(2)
        // 초기에 값을 하나씩 할당하고, 그 후에는 immutable로만 사용하고 싶을 때
    }

    // 구현체 사용하기
    val n = ArrayList<Int>()
    val o = ArrayDeque<Int>()

}