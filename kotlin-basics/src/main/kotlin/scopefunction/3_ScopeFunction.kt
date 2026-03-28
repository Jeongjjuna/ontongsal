package scopefunction

class DatabaseClient {
    var url: String? = null
    var username: String? = null
    var password: String? = null

    fun connect(): Boolean {
        println("connecting...")
        println("connected...")
        return true
    }
}

fun main() {

    // let : null 이 아닌 경우에만 동작한다.
    val name: String? = "안녕"
    val result: Int? = name?.let {
        println(it)

        1234  // return 1234 라고 해준것과 같다.
    }
    println(result)


    // run : 수신 객체의 프로퍼티를 구성하거나, 새로운 결과를 반환하고 싶을 때
    val connected = DatabaseClient().run {
        this.url = "localhost:3306"
        this.username = "mysql"
        password = "1234" // this 생략 가능
        connect()
    }
    println(connected)

    // with : 반환 결과 없이, 내부에서 다른 함수를 호출하고 싶을 때 사용
    val nickname = "hun"
    with(nickname) {
        println("size = $length")
    }

}