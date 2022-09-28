package anthill.Anthill.api.service;

import anthill.Anthill.api.dto.board.*;
import org.apache.tomcat.websocket.AuthenticationException;

public interface BoardService {
    Long posting(BoardRequestDTO boardRequestDTO);

    void changeInfo(BoardUpdateDTO boardUpdateDTO) throws AuthenticationException;

    void delete(BoardDeleteDTO boardDeleteDTO) throws AuthenticationException;

    BoardPageResponseDTO paging(int id);

    BoardResponseDTO select(Long id);

    void updateHitByBoardId(Long id);
}
