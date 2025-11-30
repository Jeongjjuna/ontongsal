package yjh.ontongsal.domain.lotto

import yjh.ontongsal.domain.lotto.command.PrizePolicyCommand

data class PrizePolicy(
    val firstPercent: Int,
    val secondPercent: Int,
    val thirdPercent: Int,
    val fourthPercent: Int,
    val fifthPercent: Int,
) {
    init {
        val total = firstPercent + secondPercent + thirdPercent + fourthPercent + fifthPercent
        require(total == 100) { "상금 비율의 총합은 반드시 100% 여야 합니다. (현재: $total)" }

        listOf(firstPercent, secondPercent, thirdPercent, fourthPercent, fifthPercent).forEach {
            require(it >= 0) { "상금 비율은 음수가 될 수 없습니다." }
        }
    }

    companion object {
        fun of(command: PrizePolicyCommand) = PrizePolicy(
            firstPercent = command.firstPercent,
            secondPercent = command.secondPercent,
            thirdPercent = command.thirdPercent,
            fourthPercent = command.fourthPercent,
            fifthPercent = command.fifthPercent,
        )
    }
}
