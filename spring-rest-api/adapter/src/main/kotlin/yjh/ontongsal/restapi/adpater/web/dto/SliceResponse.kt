package yjh.ontongsal.restapi.adpater.web.dto

data class SliceResponse<T>(
    val content: List<T>,
    val hasNext: Boolean,
)
