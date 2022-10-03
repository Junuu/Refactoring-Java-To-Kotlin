package anthill.Anthill.api.dto.board

data class BoardResponseDTO(
    val id : Long,
    val title : String,
    val content : String,
    val writer : String,
    val hits : Long,
)
