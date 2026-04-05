package yjh.ontongsal.restapi.domain.support

data class Slice<T>(
    val content: List<T>,
    val hasNext: Boolean,
)
