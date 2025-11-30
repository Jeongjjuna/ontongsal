package yjh.ontongsal.domain.lotto

import java.time.Instant

data class CreateInfo(
    val createdAt: Instant,
    val createdBy: String,
) {
    companion object {
        fun now(user: String): CreateInfo = CreateInfo(
            createdAt = Instant.now(),
            createdBy = user
        )
    }
}

// 수정 정보 VO
data class UpdateInfo(
    val updatedAt: Instant,
    val updatedBy: String,
) {
    companion object {
        fun now(user: String): UpdateInfo = UpdateInfo(
            updatedAt = Instant.now(),
            updatedBy = user
        )
    }

    fun update(user: String): UpdateInfo = UpdateInfo.now(user)
}
