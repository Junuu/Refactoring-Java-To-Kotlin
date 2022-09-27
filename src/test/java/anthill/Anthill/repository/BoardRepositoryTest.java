package anthill.Anthill.repository;

import anthill.Anthill.db.domain.board.Board;
import anthill.Anthill.db.domain.member.Address;
import anthill.Anthill.db.domain.member.Member;
import anthill.Anthill.db.repository.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class BoardRepositoryTest {

    final private String test = "test";

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void 게시물_생성_테스트() {
        //given
        Board board = getBoard(test);
        board.setMember(makeMember(test));
        boardRepository.save(board);

        //when
        List<Board> boards = boardRepository.findAll();

        //then
        Assertions.assertThat(boards.size())
                  .isEqualTo(1);
    }

    @Test
    public void 게시물_조회_테스트() {
        //given

        Board board = getBoard(test);
        board.setMember(makeMember(test));
        boardRepository.save(board);

        Board findBoard = boardRepository.findAll()
                                         .get(0);

        Assertions.assertThat(findBoard.getTitle())
                  .isEqualTo(test);
        Assertions.assertThat(findBoard.getContent())
                  .isEqualTo(test);

    }

    @Test
    public void 게시물_수정_테스트() {
        //given
        Board board = getBoard(test);
        board.setMember(makeMember(test));
        boardRepository.save(board);

        //when
        board.changeInfo("changed", "changed");

        //then
        Assertions.assertThat(board.getMember()
                                   .getBoards()
                                   .get(0)
                                   .getTitle())
                  .isEqualTo("changed");

        Assertions.assertThat(board.getTitle())
                  .isEqualTo(boardRepository.findAll()
                                            .get(0)
                                            .getTitle());

    }

    @Test
    public void 게시물_삭제_테스트() {
        //given
        Board board = getBoard(test);
        board.setMember(makeMember(test));
        boardRepository.save(board);

        //when
        board.deleteMember();
        boardRepository.delete(board);

        //then
        Assertions.assertThat(board.getMember()
                                   .getBoards()
                                   .size())
                  .isEqualTo(0);

        Assertions.assertThat(boardRepository.findAll()
                                             .size())
                  .isEqualTo(0);
    }

    @Test
    public void 특정_게시물이_동시에_삭제된경우_정상동작() {
        //given
        Board board = getBoard(test);
        board.setMember(makeMember(test));
        boardRepository.save(board);

        //when
        board.deleteMember();
        boardRepository.delete(board);
        Assertions.assertThat(boardRepository.findAll()
                                             .size())
                  .isEqualTo(0);

        try {
            board.deleteMember();
            boardRepository.delete(board);
        } catch (Exception e) {
            fail();
        }


    }

    @Test
    public void 작성자이름으로_여러게시물_조회_테스트() {
        //given
        Board board1 = getBoard(test);
        Board board2 = getBoard(test);
        Member member = makeMember(test);
        board1.setMember(member);
        board2.setMember(member);
        boardRepository.save(board1);
        boardRepository.save(board2);

        //when
        List<Board> boardsByWriter = boardRepository.findByWriter(test);

        //then
        Assertions.assertThat(boardsByWriter.size())
                  .isEqualTo(2);
    }


    private Board getBoard(String settingValue) {

        Board board = Board.builder()
                           .title(settingValue)
                           .content(settingValue)
                           .writer(settingValue)
                           .hits(0L)
                           .build();

        return board;
    }

    private Member makeMember(String settingValue) {
        Member member = Member.builder()
                              .userId(settingValue)
                              .name(settingValue)
                              .nickName(settingValue)
                              .password(settingValue)
                              .phoneNumber(settingValue)
                              .address(new Address("a1", "a2", "a3"))
                              .build();
        return member;
    }

}