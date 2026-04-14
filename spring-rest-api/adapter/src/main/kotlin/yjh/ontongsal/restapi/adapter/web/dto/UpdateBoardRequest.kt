package yjh.ontongsal.restapi.adapter.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import yjh.ontongsal.restapi.domain.BoardName

data class UpdateBoardRequest(
    @field:NotNull(message = "게시판 이름은 필수입니다.")
    @field:NotBlank(message = "게시판 이름은 비어있을 수 없습니다.")
    val name: String,
) {
    fun toBoardName(): BoardName = BoardName(name)
}
