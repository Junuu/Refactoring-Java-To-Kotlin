package anthill.Anthill.domain.board.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class BoardRequestDTO(
    @field:NotNull(message = "사용자 아이디는 필수값입니다.") val memberId: Long,
    @field:NotBlank(message = "제목을 입력해주세요") val title:  String,
    @field:NotBlank(message = "본문을 입력해주세요") var content: String,
    var writer: String,
)
