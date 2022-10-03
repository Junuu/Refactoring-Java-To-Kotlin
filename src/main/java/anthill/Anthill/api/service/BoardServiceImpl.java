package anthill.Anthill.api.service;

import anthill.Anthill.api.dto.board.*;
import anthill.Anthill.db.domain.board.Board;
import anthill.Anthill.db.domain.member.Member;
import anthill.Anthill.db.repository.BoardRepository;
import anthill.Anthill.db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long posting(BoardRequestDTO boardRequestDTO) {
        Member member = memberRepository.findById(boardRequestDTO.getMemberId())
                                        .orElseThrow(() -> new IllegalArgumentException());
        Board board = new Board(
                boardRequestDTO.getTitle(),
                boardRequestDTO.getContent(),
                boardRequestDTO.getWriter(),
                0L,
                member = member
        );

        return boardRepository.save(board)
                              .getId();
    }

    @Override
    public void changeInfo(BoardUpdateDTO boardUpdateDTO) throws AuthenticationException {
        Board board = boardRepository.findById(boardUpdateDTO.getId())
                                         .orElseThrow(() -> new IllegalArgumentException());

        if (board.getMember()
                 .getId() != boardUpdateDTO.getMemberId()) {
            throw new AuthenticationException("권한 없음");
        }
        board.changeInfo(boardUpdateDTO.getTitle(), boardUpdateDTO.getContent());
    }

    @Override
    public void delete(BoardDeleteDTO boardDeleteDto) throws AuthenticationException {
        Board board = boardRepository.findById(boardDeleteDto.getId())
                                         .orElseThrow(() -> new IllegalArgumentException());

        if (board.getMember()
                 .getId() != boardDeleteDto.getMemberId()) {
            throw new AuthenticationException("권한 없음");
        }

        boardRepository.deleteById(boardDeleteDto.getId());
    }

    @Override
    public BoardPageResponseDTO paging(int pageIndex) {

        Pageable curPage = PageRequest.of(pageIndex, 10, Sort.by("id")
                                                             .descending());
        Page<Board> result = boardRepository.findAll(curPage);

        if (pageIndex > result.getTotalPages()
        ) {
            throw new IllegalArgumentException("유효하지 않은 페이지");
        }

        BoardPageResponseDTO map = BoardPageResponseDTO.toBoardPagingDTO(result);
        return map;
    }

    @Override
    public BoardResponseDTO select(Long id) {
        Board board = boardRepository.findById(id)
                                         .orElseThrow(() -> new IllegalArgumentException());
        return board.toBoardResponseDTO();
    }

    @Override
    public void updateHitByBoardId(Long id) {
        boardRepository.updateHitByBoardId(id);
    }


}
