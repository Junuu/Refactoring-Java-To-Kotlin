package anthill.Anthill.domain.board.service

import anthill.Anthill.domain.board.dto.BoardPageResponseDTO
import anthill.Anthill.domain.board.dto.BoardResponseDTO
import anthill.Anthill.domain.board.repository.BoardRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BoardQueryService(
    val boardRepository: BoardRepository,
    ) {
    fun paging(pageIndex: Int): BoardPageResponseDTO {
        val curPage: Pageable = PageRequest.of(pageIndex, 10, Sort.by("id").descending())
        val result = boardRepository.findAll(curPage)
        require(pageIndex <= result.totalPages) {"유효하지 않은 페이지" }
        return BoardPageResponseDTO.toBoardPagingDTO(result)
    }

    fun select(id: Long): BoardResponseDTO {
        val board = boardRepository.findById(id).orElseThrow { IllegalArgumentException() }
        return board.toBoardResponseDTO()
    }
}