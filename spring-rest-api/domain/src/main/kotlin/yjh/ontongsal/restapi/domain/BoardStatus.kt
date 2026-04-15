package yjh.ontongsal.restapi.domain

enum class BoardStatus {
    ACTIVE,
    INACTIVE;

    fun isInactive() = this == INACTIVE
}
