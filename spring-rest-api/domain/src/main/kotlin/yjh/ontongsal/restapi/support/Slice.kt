package yjh.ontongsal.restapi.support

data class Slice<T>(
    val content: List<T>,
    val hasNext: Boolean,
)
