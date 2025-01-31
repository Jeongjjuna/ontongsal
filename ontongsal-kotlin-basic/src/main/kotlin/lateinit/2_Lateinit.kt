package lateinit

fun main() {

    // 1. 초기값을 주지 않을 경우에는 지연 초기화를 사용할 수 있다.
    class Name {
        lateinit var name: String

        // 초기화하지 않고 사용하면 UninitializedPropertyAccessException 발생
        fun test() {
            if (::name.isInitialized) { // 코틀린은 초기화 되었는지 확인할 수 있도록 문법을 제공한다.
                print(name)
            }
        }
    }

}