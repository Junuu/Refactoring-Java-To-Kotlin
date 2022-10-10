package anthill.Anthill.api.service

import TestFixture
import anthill.Anthill.db.domain.member.Address
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
        boardService = BoardService(
            boardRepository = boardRepository,
            memberRepository = memberRepository,
        )
    }

    private fun saveMember(): Long {
        val savedMember = memberRepository.save(
            Member(
                userId = "junwoo",
                password = "123456789",
                phoneNumber = "01012345678",
                name = "김준우",
                nickName = "junwoo",
                address = Address(
                    streetNameAddress = "경기도 시흥시",
                    detailAddress = "XX아파트 XX호",
                    zipCode = "429-010",
                )
            )
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
    @ValueSource(ints = [-100, -1, 100])
    fun `음수 또는 페이지 크기보다 큰 index를 넣으면 IllegalArgumentException 발생`(input: Int) {
        val savedMemberId = saveMember()
        boardService.posting(TestFixture.boardRequestDTO(savedMemberId))

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            boardService.paging(input)
        }
    }

    @Test
    fun `0~페이지 크기보다 작은 범위의 index를 넣으면 페이징 조회에 성공한다`() {
        val savedMemberId = saveMember()
        repeat(43) {
            boardService.posting(TestFixture.boardRequestDTO(savedMemberId))
        }

        val firstPaging = boardService.paging(0)
        val lastPaging = boardService.paging(4)

        Assertions.assertEquals(10, firstPaging.contents.size)
        Assertions.assertEquals(3, lastPaging.contents.size)
    }

    @Test
    fun `페이지된 상세페이지를 조회할 수 있다`() {
        val savedMemberId = saveMember()
        boardService.posting(TestFixture.boardRequestDTO(savedMemberId))
        val userInputTitle = "userInputTitle"

        val pagingResult = boardService.paging(0)

        Assertions.assertEquals(pagingResult.size, 10)
        Assertions.assertEquals(pagingResult.totalPage, 1)
        Assertions.assertEquals(pagingResult.totalElements, 1)
        Assertions.assertEquals(pagingResult.curPage, 0)
        Assertions.assertEquals(pagingResult.contents.size, 1)
        Assertions.assertEquals(pagingResult.contents[0].title, userInputTitle)
    }

    @Test
    fun `단일 게시글을 조회할 수 있다`() {
        val savedMemberId = saveMember()
        val savedBoardId = boardService.posting(TestFixture.boardRequestDTO(savedMemberId))

        val result = boardService.select(savedBoardId)

        Assertions.assertEquals(result.id, savedBoardId)
    }

    @Test
    fun `단일 게시글을 조회할 때 존재하지 않으면 IllegalArgumentException 발생`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            boardService.select(NOT_EXIST_ID)
        }
    }

    @Test
    fun `게시글 조회수를 증가시킬 수 있다`() {
        val savedMemberId = saveMember()
        val savedBoardId = boardService.posting(TestFixture.boardRequestDTO(savedMemberId))

        boardService.updateHitByBoardId(savedBoardId)
        entityManager.flush()
        entityManager.clear()
        val result = boardService.select(savedBoardId)

        Assertions.assertEquals(result.hits, 1)
    }

//    @Test
//    fun `게시글 조회수를 증가시킬 때 데이터 동시성이 지켜진다`(){
//        val savedMemberId = saveMember()
//        val savedBoardId = boardService.posting(TestFixture.boardRequestDTO(savedMemberId))
//        entityManager.flush()
//
//        //100*1000만회
//        runBlocking {
//            GlobalScope.massiveRun {
//                boardService.updateHitByBoardId(savedBoardId)
//            }
//        }
//        entityManager.flush()
//        val result = boardService.select(savedBoardId)
//
//        Assertions.assertEquals(result.hits, 100*1000)
//    }
//
//    suspend fun CoroutineScope.massiveRun(action: suspend () -> Unit) {
//        val n = 100  // number of coroutines to launch
//        val k = 1000 // times an action is repeated by each coroutine
//        val time = measureTimeMillis {
//            val jobs = List(n) {
//                launch {
//                    repeat(k) { action() }
//                }
//            }
//            jobs.forEach { it.join() }
//        }
//        println("Completed ${n * k} actions in $time ms")
//    }

    companion object {
        const val NOT_EXIST_ID = -1L
    }

}