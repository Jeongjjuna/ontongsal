package yjh.ontongsal.domain.lotto

import java.time.Instant
import yjh.ontongsal.domain.lotto.command.LottoCreateCommand
import yjh.ontongsal.domain.lotto.command.LottoUpdateCommand

class Lotto(
    val id: Int,
    val round: Int,
    var isActive: Boolean = false,
    var startAt: Instant,
    var endAt: Instant,
    var drawnAt: Instant? = null,
    var winningNumbers: LottoNumbers? = null,
    var bonusNumber: LottoNumber? = null,
    var prizePolicy: PrizePolicy,
    val createInfo: CreateInfo,
    var updateInfo: UpdateInfo,
) {
    init {
        require(round > 0) { "회차는 1 이상이어야 합니다." }
        require(startAt < endAt) { "startAt < endAt 이어야 합니다." }
    }

    fun draw(winningNumbers: LottoNumbers, bonusNumber: LottoNumber, drawnBy: String) {
        require(this.winningNumbers == null) { "이미 당첨 번호가 존재합니다." }

        this.winningNumbers = winningNumbers
        this.bonusNumber = bonusNumber
        this.drawnAt = Instant.now()
        this.updateInfo = UpdateInfo.now(drawnBy)
    }

    fun update(command: LottoUpdateCommand) {
        this.isActive = command.isActive
        this.startAt = command.startAt
        this.endAt = command.endAt
        this.prizePolicy = PrizePolicy.of(command.prizePolicyCommand)
        this.updateInfo = UpdateInfo.now(command.updatedBy)
    }

    fun canEntry(now: Instant): Boolean {
        if (!this.isActive) {
            return false
        }
        if (now.isBefore(this.startAt) || now.isAfter(this.endAt)) {
            return false
        }
        return true
    }

    fun canDraw(now: Instant): Boolean {
        if (now.isBefore(this.endAt)) {
            return false
        }
        if (this.winningNumbers != null){
            return false
        }
        return true
    }

    companion object {
        fun of(command: LottoCreateCommand) = Lotto(
            id = command.id,
            round = command.round,
            isActive = command.isActive,
            startAt = command.startAt,
            endAt = command.endAt,
            prizePolicy = PrizePolicy.of(command.prizePolicyCommand),
            createInfo = CreateInfo(Instant.now(), command.createdBy),
            updateInfo = UpdateInfo(Instant.now(), command.createdBy)
        )
    }
}
