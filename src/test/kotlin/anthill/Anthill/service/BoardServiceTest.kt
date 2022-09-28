package anthill.Anthill.service

import TestFixture
import anthill.Anthill.api.service.BoardService
import anthill.Anthill.api.service.BoardServiceImpl
import anthill.Anthill.db.domain.member.Member
import anthill.Anthill.db.repository.BoardRepository
import anthill.Anthill.db.repository.MemberRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@DataJpaTest
class BoardServiceTest @Autowired constructor(
    val boardRepository : BoardRepository,
    val memberRepository: MemberRepository,
) {
    private lateinit var boardService: BoardService

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun setUp(){
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
    fun `게시글의 제목,내용,작성자,조회수가 올바르게 저장된다`(){
        val userInputTitle = "userInputTitle"
        val userInputContent = "userInputContent"
        val userInputWriter = "userInputWriter"
        val savedMemberId = saveMember()
        val boardRequestDTO = TestFixture.boardRequestDTO(savedMemberId = savedMemberId)

        val savedBoardId = boardService.posting(boardRequestDTO)
        val savedBoard = boardRepository.findById(savedBoardId).get()

        Assertions.assertNotNull(savedBoard)
        Assertions.assertEquals(savedBoard.id, savedBoardId)
        Assertions.assertEquals(savedBoard.title, userInputTitle)
        Assertions.assertEquals(savedBoard.content, userInputContent)
        Assertions.assertEquals(savedBoard.writer, userInputWriter)
    }

    @Test
    fun `존재하지 않는 회원 ID가 요청으로 들어오면 IllegalArgumentException 발생`(){
        val boardRequestDTO = TestFixture.boardRequestDTO(savedMemberId = 1L)

        Assertions.assertThrows(IllegalArgumentException::class.java){
            val savedBoardId = boardService.posting(boardRequestDTO)
        }
    }

    @Test
    fun `게시글의 제목,정보를 변경할 수 있다`(){
        val savedMemberId = saveMember()
        val savedBoardId = boardService.posting(TestFixture.boardRequestDTO(savedMemberId))
        val boardUpdateDTO = TestFixture.boardUpdateDTO(savedBoardId)
        val userInputUpdateTitle = "userInputUpdateTitle"
        val userInputUpdateContent = "userInputUpdateContent"

        boardService.changeInfo(boardUpdateDTO)
        entityManager.flush()
        entityManager.clear()
        val result = boardRepository.findAll()[0]


        Assertions.assertEquals(result.title, userInputUpdateTitle)
        Assertions.assertEquals(result.content, userInputUpdateContent)
    }


}