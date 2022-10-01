package anthill.Anthill.service

import TestFixture
import anthill.Anthill.api.service.BoardService
import anthill.Anthill.api.service.BoardServiceImpl
import anthill.Anthill.db.domain.member.Member
import anthill.Anthill.db.repository.BoardRepository
import anthill.Anthill.db.repository.MemberRepository
import org.apache.tomcat.websocket.AuthenticationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@DataJpaTest
class BoardServiceTest @Autowired constructor(
    val boardRepository: BoardRepository,
    val memberRepository: MemberRepository,
) {
    private lateinit var boardService: BoardService

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun setUp() {
        boardService = BoardServiceImpl(
            boardRepository,
            memberRepository,
        )
    }

    private fun saveMember(): Long {
        val savedMember = memberRepository.save(
            Member.builder()
                .userId("junwoo")
                .password("123456789")
                .phoneNumber("01012345678")
                .name("김준우")
                .nickName("junwoo")
                .build()
        )
        return savedMember.id
    }

    @Test
    fun `게시글의 제목,내용,작성자,조회수가 올바르게 저장된다`() {
        val userInputTitle = "userInputTitle"
        val userInputContent = "userInputContent"
        val userInputWriter = "userInputWriter"
        val savedMemberId = saveMember()
        val boardRequestDTO = TestFixture.boardRequestDTO(
            savedMemberId = savedMemberId,
        )

        val savedBoardId = boardService.posting(boardRequestDTO)
        val savedBoard = boardRepository.findById(savedBoardId).get()

        Assertions.assertNotNull(savedBoard)
        Assertions.assertEquals(savedBoard.id, savedBoardId)
        Assertions.assertEquals(savedBoard.title, userInputTitle)
        Assertions.assertEquals(savedBoard.content, userInputContent)
        Assertions.assertEquals(savedBoard.writer, userInputWriter)
    }

    @Test
    fun `존재하지 않는 회원 ID가 요청으로 들어오면 IllegalArgumentException 발생`() {
        val boardRequestDTO = TestFixture.boardRequestDTO(
            savedMemberId = NOT_EXIST_ID,
        )

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            boardService.posting(boardRequestDTO)
        }
    }

    @Test
    fun `게시글의 제목,정보를 변경할 수 있다`() {
        val savedMemberId = saveMember()
        val savedBoardId = boardService.posting(TestFixture.boardRequestDTO(savedMemberId))
        val boardUpdateDTO = TestFixture.boardUpdateDTO(
            savedBoardId = savedBoardId,
            savedMemberId = savedMemberId,
        )
        val userInputUpdateTitle = "userInputUpdateTitle"
        val userInputUpdateContent = "userInputUpdateContent"

        boardService.changeInfo(boardUpdateDTO)
        entityManager.flush()
        entityManager.clear()
        val result = boardRepository.findAll()[0]


        Assertions.assertEquals(result.title, userInputUpdateTitle)
        Assertions.assertEquals(result.content, userInputUpdateContent)
    }

    @Test
    fun `존재 하지 않는 게시판 ID가 요청으로 들어오면 IllegalArgumentException 발생`() {
        val boardUpdateDTO = TestFixture.boardUpdateDTO(
            savedBoardId = NOT_EXIST_ID,
            savedMemberId = NOT_EXIST_ID,
        )

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            boardService.changeInfo(boardUpdateDTO)
        }
    }

    @Test
    fun `게시글을 쓴사람의 id와 변경하는 사람의 id가 다른경우 AuthenticationException 발생`() {
        val savedMemberId = saveMember()
        val savedBoardId = boardService.posting(TestFixture.boardRequestDTO(savedMemberId))
        val boardUpdateDTO = TestFixture.boardUpdateDTO(
            savedBoardId = savedBoardId,
            savedMemberId = NOT_EXIST_ID,
        )
        entityManager.flush()

        Assertions.assertThrows(AuthenticationException::class.java) {
            boardService.changeInfo(boardUpdateDTO)
        }
    }

    @Test
    fun `게시글을 삭제하려고 하는데 해당 게시물이 없으면 IllegalArgumentException 발생`() {
        val boardDeleteDTO = TestFixture.boardDeleteDTO(
            savedBoardId = NOT_EXIST_ID,
            savedMemberId = NOT_EXIST_ID,
        )

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            boardService.delete(boardDeleteDTO)
        }
    }

    @Test
    fun `게시글을 삭제하려고 하는데 삭제 권한이 없으면 AuthenticationException 발생`() {
        val savedMemberId = saveMember()
        val savedBoardId = boardService.posting(TestFixture.boardRequestDTO(savedMemberId))
        val boardDeleteDTO = TestFixture.boardDeleteDTO(
            savedBoardId = savedBoardId,
            savedMemberId = NOT_EXIST_ID,
        )

        Assertions.assertThrows(AuthenticationException::class.java) {
            boardService.delete(boardDeleteDTO)
        }
    }

    @Test
    fun `게시글을 생성하고 삭제`() {
        val savedMemberId = saveMember()
        val savedBoardId = boardService.posting(TestFixture.boardRequestDTO(savedMemberId))
        val boardDeleteDTO = TestFixture.boardDeleteDTO(
            savedBoardId = savedBoardId,
            savedMemberId = savedMemberId,
        )

        boardService.delete(boardDeleteDTO)

        val repositoryDataCount = boardRepository.findAll().size
        Assertions.assertEquals(repositoryDataCount, 0)
    }

    @ParameterizedTest
    @ValueSource(ints = [-100,-1])
    fun `0보다 큰 페이지 index를 넣어주지 않으면 IllegalArgumentException 발생`(input: Int){
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            boardService.paging(input)
        }
    }




    companion object {
        const val NOT_EXIST_ID = -1L
    }

}