package anthill.Anthill.api.service;

import anthill.Anthill.api.dto.board.*;
import anthill.Anthill.api.service.BoardService;
import anthill.Anthill.api.service.BoardServiceImpl;
import anthill.Anthill.db.domain.board.Board;
import anthill.Anthill.db.domain.member.Member;
import anthill.Anthill.db.repository.BoardRepository;
import anthill.Anthill.db.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class BoardServiceImplTest {

    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    long savedMemberId;

    @BeforeEach
    void setUp() {
        boardService = new BoardServiceImpl(boardRepository, memberRepository);
        savedMemberId = saveMember();
    }

    @Test
    void 페이징() {
        //given
        BoardRequestDTO boardRequestDTO = makeBoardRequestDTO(savedMemberId);
        for (int i = 0; i < 43; i++) {
            boardService.posting(boardRequestDTO);
        }

        //when
        BoardPageResponseDTO firstPaging = boardService.paging(0);

        Pageable LastPageWithTenElements = PageRequest.of(4, 10);
        BoardPageResponseDTO lastPaging = boardService.paging(4);

        //then
        Assertions.assertThat(10)
                  .isEqualTo(firstPaging.getContents()
                                        .size());
        Assertions.assertThat(3)
                  .isEqualTo(lastPaging.getContents()
                                       .size());
    }

    @Test
    void 페이징음수() {
        //given

        BoardRequestDTO boardRequestDTO = makeBoardRequestDTO(savedMemberId);
        boardService.posting(boardRequestDTO);

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            boardService.paging(-1);
        });

    }

    @Test
    void 페이징초과() {
        //given
        BoardRequestDTO boardRequestDTO = makeBoardRequestDTO(savedMemberId);
        boardService.posting(boardRequestDTO);

        //when
        assertThrows(IllegalStateException.class, () -> {
            //when
            boardService.paging(100);
        });

    }


    @Test
    void 게시글_작성_테스트() {
        //given
        BoardRequestDTO boardRequestDTO = makeBoardRequestDTO(savedMemberId);

        //when
        boardService.posting(boardRequestDTO);

        //then
        List<Board> result = boardRepository.findAll();
        Assertions.assertThat(result.size())
                  .isEqualTo(1);
        Assertions.assertThat(result.get(0)
                                    .getMember())
                  .isNotNull();
    }

    @Test
    void 게시글_조회_테스트() throws Exception {
        //given
        BoardRequestDTO boardRequestDTO = makeBoardRequestDTO(savedMemberId);
        boardService.posting(boardRequestDTO);

        Board result = boardRepository.findAll()
                                      .get(0);
        final Long id = result.getId();

        //when
        BoardResponseDTO boardResponseDTO = boardService.select(id);

        //then
        Assertions.assertThat(boardResponseDTO.getTitle())
                  .isEqualTo("제목");
        Assertions.assertThat(boardResponseDTO.getContent())
                  .isEqualTo("본문");
    }

    @Test
    void 게시글_수정_테스트() throws Exception {
        //given
        BoardRequestDTO boardRequestDTO = makeBoardRequestDTO(savedMemberId);
        boardService.posting(boardRequestDTO);

        //when
        Board result = boardRepository.findAll()
                                      .get(0);
        final Long id = result.getId();

        boardService.changeInfo(makeBoardUpdateDTO(id));
        boardRepository.flush();

        result = boardRepository.findAll()
                                .get(0);

        //then
        final String changedTitle = "changedTitle";
        final String changedContent = "changedContent";

        Assertions.assertThat(result.getTitle())
                  .isEqualTo(changedTitle);
        Assertions.assertThat(result.getContent())
                  .isEqualTo(changedContent);

    }

    @Test
    void 게시글_삭제_테스트() throws Exception {
        //given
        BoardRequestDTO boardRequestDTO = makeBoardRequestDTO(savedMemberId);
        boardService.posting(boardRequestDTO);

        //when
        final Long id = boardRepository.findAll()
                                       .get(0)
                                       .getId();
        boardService.delete(makeBoardDeleteDTO(id));

        //then
        Assertions.assertThat(boardRepository.findAll()
                                             .size())
                  .isEqualTo(0);
    }


    private BoardDeleteDTO makeBoardDeleteDTO(Long id) {
        BoardDeleteDTO boardDeleteDTO = BoardDeleteDTO.builder()
                                                      .id(id)
                                                      .memberId(id)
                                                      .build();
        return boardDeleteDTO;
    }

    private BoardUpdateDTO makeBoardUpdateDTO(Long id) {
        BoardUpdateDTO boardUpdateDTO = BoardUpdateDTO.builder()
                                                      .id(id)
                                                      .title("changedTitle")
                                                      .content("changedContent")
                                                      .writer("작성자")
                                                      .build();
        return boardUpdateDTO;
    }


    private BoardRequestDTO makeBoardRequestDTO(Long savedMemberId) {

        BoardRequestDTO boardRequestDTO = BoardRequestDTO.builder()
                                                         .memberId(savedMemberId)
                                                         .title("제목")
                                                         .content("본문")
                                                         .writer("작성자")
                                                         .build();

        return boardRequestDTO;
    }

    private Long saveMember() {
        Member savedMember = memberRepository.save(
                Member.builder()
                      .userId("junwoo")
                      .password("123456789")
                      .phoneNumber("01012345678")
                      .name("김준우")
                      .nickName("junwoo")
                      .build()
        );
        return savedMember.getId();
    }

}