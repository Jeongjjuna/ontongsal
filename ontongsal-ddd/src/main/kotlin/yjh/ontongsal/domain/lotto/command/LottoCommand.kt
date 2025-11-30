package yjh.ontongsal.domain.lotto.command

import java.time.Instant

data class LottoCreateCommand(
    val id: Int,
    val round: Int,
    val isActive: Boolean,
    val startAt: Instant,
    val endAt: Instant,
    val prizePolicyCommand: PrizePolicyCommand,
    val createdBy: String,
)

data class LottoUpdateCommand(
    val isActive: Boolean,
    val startAt: Instant,
    val endAt: Instant,
    val prizePolicyCommand: PrizePolicyCommand,
    val updatedBy: String,
)

data class PrizePolicyCommand(
    val firstPercent: Int,
    val secondPercent: Int,
    val thirdPercent: Int,
    val fourthPercent: Int,
    val fifthPercent: Int,
)
