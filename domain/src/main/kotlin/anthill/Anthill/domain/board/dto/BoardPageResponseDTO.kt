package anthill.Anthill.domain.board.dto

import anthill.Anthill.domain.board.entity.Board
import org.springframework.data.domain.Page

class BoardPageResponseDTO(
    val contents: List<BoardInfoDTO>,
    val totalPage: Int,
    val totalElements: Long,
    val curPage: Int,
    val size: Int,
) {
    companion object {
        fun toBoardPagingDTO(page: Page<Board>): BoardPageResponseDTO {
            return BoardPageResponseDTO(
                contents = page.content.map {
                    BoardInfoDTO.toBoardInfoDTO(it)
                }.toList(),
                totalPage = page.totalPages,
                totalElements = page.totalElements,
                curPage = page.number,
                size = page.size
            )
        }
    }
}