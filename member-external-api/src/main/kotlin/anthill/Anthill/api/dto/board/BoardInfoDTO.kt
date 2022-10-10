package anthill.Anthill.api.dto.board

import anthill.Anthill.db.domain.board.Board

class BoardInfoDTO(
    val id : Long,
    val title : String,
    val content : String,
    val writer : String,
    val hits : Long,
) {
    companion object{
        fun toBoardInfoDTO(board : Board) : BoardInfoDTO{
            return BoardInfoDTO(
                id = board.id,
                title = board.title,
                content = board.content,
                writer = board.writer,
                hits = board.hits,
            )
        }
    }
}