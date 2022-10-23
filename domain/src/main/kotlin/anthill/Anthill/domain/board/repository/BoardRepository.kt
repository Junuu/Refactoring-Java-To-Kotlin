package anthill.Anthill.domain.board.repository

import anthill.Anthill.domain.board.entity.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface BoardRepository : JpaRepository<Board, Long> {
    @Modifying
    @Query("UPDATE Board b SET b.hits = b.hits + 1 WHERE b.id =:boardId")
    fun updateHitByBoardId(boardId: Long)
}