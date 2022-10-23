package anthill.Anthill.domain.board.service

import anthill.Anthill.domain.board.dto.BoardDeleteDTO
import anthill.Anthill.domain.board.dto.BoardRequestDTO
import anthill.Anthill.domain.board.dto.BoardUpdateDTO
import anthill.Anthill.domain.board.entity.Board
import anthill.Anthill.domain.board.repository.BoardRepository
import anthill.Anthill.domain.member.repository.MemberRepository
import org.apache.tomcat.websocket.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardCommandService(
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

    fun updateHitByBoardId(id: Long) {
        boardRepository.updateHitByBoardId(id)
    }
}