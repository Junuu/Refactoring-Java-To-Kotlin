package anthill.Anthill.api.dto.board

data class BoardUpdateDTO(
    val id : Long,
    val title : String,
    val content : String,
    val writer : String,
    val memberId : Long,
)
