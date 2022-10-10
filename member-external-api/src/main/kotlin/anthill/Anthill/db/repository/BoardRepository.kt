package anthill.Anthill.db.repository

import anthill.Anthill.db.domain.board.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface BoardRepository : JpaRepository<Board, Long> {
    fun findByWriter(writer: String): List<Board>

    @Modifying
    @Query("UPDATE Board b SET b.hits = b.hits + 1 WHERE b.id =:boardId")
    fun updateHitByBoardId(boardId: Long)
}