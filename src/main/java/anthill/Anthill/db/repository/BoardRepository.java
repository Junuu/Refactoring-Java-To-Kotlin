package anthill.Anthill.db.repository;

import anthill.Anthill.db.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByWriter(String writer);

    @Modifying
    @Query("UPDATE Board b SET b.hits = b.hits + 1 WHERE b.id =:boardId")
    void updateHitByBoardId(Long boardId);
}
