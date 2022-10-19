package anthill.Anthill.domain.board.dto

data class BoardUpdateDTO(
    val id : Long,
    val title : String,
    val content : String,
    val writer : String,
    val memberId : Long,
)
