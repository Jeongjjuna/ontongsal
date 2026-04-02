package yjh.ontongsal.restapi.web.dto

data class SliceResponse<T>(
    val content: List<T>,
    val hasNext: Boolean,
)
