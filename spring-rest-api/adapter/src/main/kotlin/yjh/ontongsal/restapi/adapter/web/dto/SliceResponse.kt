package yjh.ontongsal.restapi.adapter.web.dto

data class SliceResponse<T>(
    val content: List<T>,
    val hasNext: Boolean,
)
