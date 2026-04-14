package yjh.ontongsal.restapi.domain

enum class BoardStatus {
    ACTIVE,
    DELETED;

    fun isDeleted() = this == DELETED
}
