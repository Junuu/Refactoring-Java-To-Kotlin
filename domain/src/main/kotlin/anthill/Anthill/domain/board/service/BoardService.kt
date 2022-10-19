package anthill.Anthill.domain.board.service

import anthill.Anthill.domain.board.dto.*
import anthill.Anthill.domain.board.entity.Board
import anthill.Anthill.domain.board.repository.BoardRepository
import anthill.Anthill.domain.member.repository.MemberRepository
import org.apache.tomcat.websocket.AuthenticationException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardService(
    val boardRepository: BoardRepository,
    val memberRepository: MemberRepository,
) {
    fun posting(boardRequestDTO: BoardRequestDTO): Long {
        var member = memberRepository.findById(boardRequestDTO.memberId)
            .orElseThrow { IllegalArgumentException() }
        val board = Board(
            title = boardRequestDTO.title,
            content = boardRequestDTO.content,
            writer = boardRequestDTO.writer,
            hits = 0L,
            member = member,
        )
        return boardRepository.save(board).id
    }

    fun changeInfo(boardUpdateDTO: BoardUpdateDTO) {
        val board = boardRepository.findById(boardUpdateDTO.id)
            .orElseThrow { IllegalArgumentException() }
        if (board.member.id != boardUpdateDTO.memberId) {
            throw AuthenticationException("권한 없음")
        }
        board.changeInfo(boardUpdateDTO.title, boardUpdateDTO.content)
    }

    fun delete(boardDeleteDto: BoardDeleteDTO) {
        val board = boardRepository.findById(boardDeleteDto.id).orElseThrow { IllegalArgumentException() }
        if (board.member.id != boardDeleteDto.memberId) {
            throw AuthenticationException("권한 없음")
        }
        boardRepository.deleteById(boardDeleteDto.id)
    }

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

    fun updateHitByBoardId(id: Long) {
        boardRepository.updateHitByBoardId(id)
    }
}