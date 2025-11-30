package yjh.ontongsal.domain.lotto

class LottoNumbers(numbers: List<LottoNumber>) {

    val numbers: List<LottoNumber> = numbers.toList() // 방어적 복사

    init {
        require(numbers.size == 6) { "로또 번호는 반드시 6개여야 합니다." }
        require(numbers.distinct().count() == 6) { "로또 번호는 중복될 수 없습니다." }
    }
}
