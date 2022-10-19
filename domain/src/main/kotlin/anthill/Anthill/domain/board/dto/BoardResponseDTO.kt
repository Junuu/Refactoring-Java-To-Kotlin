package anthill.Anthill.domain.board.dto

data class BoardResponseDTO(
    val id : Long,
    val title : String,
    val content : String,
    val writer : String,
    val hits : Long,
)
