package anthill.Anthill.api.controller

import TestFixture
import anthill.Anthill.api.service.BoardService
import anthill.Anthill.api.service.JwtService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureRestDocs
@WebMvcTest(BoardController::class)
class BoardControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var boardService: BoardService

    @MockBean
    lateinit var jwtService: JwtService

    @Test
    fun `게시글 작성 인증 실패`() {
        val boardRequestDTO = TestFixture.boardRequestDTO(1L)
        val body = ObjectMapper().writeValueAsString(boardRequestDTO)
        val accessTokenHeader = "access-token"
        val token = "header.payload.sign"

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/boards")
                .content(body)
                .header(accessTokenHeader, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `게시글 작성인증 성공`() {
        val boardRequestDTO = TestFixture.boardRequestDTO(1L)
        val token = "header.payload.sign"
        val accessTokenHeader = "access-token"
        val body = ObjectMapper().writeValueAsString(boardRequestDTO)
        BDDMockito.given(jwtService.isUsable(ArgumentMatchers.anyString())).willReturn(true)

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/boards")
                .content(body)
                .header(accessTokenHeader, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(
                MockMvcRestDocumentation.document(
                    "board-posting-success",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("access-token").description("로그인 시 발급된 토큰")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("memberId").description("회원아이디"),
                        PayloadDocumentation.fieldWithPath("title").description("제목"),
                        PayloadDocumentation.fieldWithPath("content").description("본문"),
                        PayloadDocumentation.fieldWithPath("writer").description("작성자")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("message").description("메시지"),
                        PayloadDocumentation.fieldWithPath("responseData").description("반환값"),
                        PayloadDocumentation.fieldWithPath("errorMessage").description("에러 메시지")
                    )
                )
            )
    }

    @Test
    fun `게시글 페이지 조회를 수행할 수 있다`() {
        val pagingId = 1
        val boardPagingDTO = TestFixture.boardPageResponseDTO()
        BDDMockito.given(boardService.paging(ArgumentMatchers.any(Int::class.java))).willReturn(boardPagingDTO)

        val resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/boards/page/{paging-id}", pagingId))

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "board-paging-success",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("paging-id").description("페이징 번호")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("message").description("메시지"),
                        PayloadDocumentation.fieldWithPath("responseData").description("반환값"),
                        PayloadDocumentation.fieldWithPath("responseData.contents.[].id").description("게시글 번호"),
                        PayloadDocumentation.fieldWithPath("responseData.contents.[].title").description("제목"),
                        PayloadDocumentation.fieldWithPath("responseData.contents.[].content").description("본문"),
                        PayloadDocumentation.fieldWithPath("responseData.contents.[].writer").description("작성자"),
                        PayloadDocumentation.fieldWithPath("responseData.contents.[].hits").description("조회수"),
                        PayloadDocumentation.fieldWithPath("responseData.totalPage").description("전체 페이지 개수"),
                        PayloadDocumentation.fieldWithPath("responseData.totalElements").description("테이블 데이터 총 개수"),
                        PayloadDocumentation.fieldWithPath("responseData.curPage").description("현재 페이지 번호"),
                        PayloadDocumentation.fieldWithPath("responseData.size").description("한 페이지당 조회할 데이터 개수"),
                        PayloadDocumentation.fieldWithPath("errorMessage").description("에러 메시지")
                    )
                )
            )
    }

    @Test
    fun `게시글 단건 조회에 성공한다`() {
        val boardId = 1L
        val boardResponseDTO = TestFixture.boardResponseDTO()
        BDDMockito.given(boardService.select(ArgumentMatchers.anyLong())).willReturn(boardResponseDTO)

        val resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/boards/{boardid}", boardId))

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "board-get-by-id-success",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("boardid").description("게시글 번호")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("message").description("메시지"),
                        PayloadDocumentation.fieldWithPath("responseData").description("반환값"),
                        PayloadDocumentation.fieldWithPath("responseData.id").description("게시글 번호"),
                        PayloadDocumentation.fieldWithPath("responseData.title").description("제목"),
                        PayloadDocumentation.fieldWithPath("responseData.content").description("본문"),
                        PayloadDocumentation.fieldWithPath("responseData.writer").description("작성자"),
                        PayloadDocumentation.fieldWithPath("responseData.hits").description("조회수"),
                        PayloadDocumentation.fieldWithPath("errorMessage").description("에러 메시지")
                    )
                )
            )
    }

    @Test
    fun `게시물 권한에 없어 수정에 실패한다`() {
        val boardUpdateDTO = TestFixture.boardUpdateDTO(
            savedBoardId = 1L,
            savedMemberId = 1L,
        )
        val accessTokenHeader = "access-token"
        val token = "header.payload.sign"
        val body = ObjectMapper().writeValueAsString(boardUpdateDTO)
        BDDMockito.given(jwtService.isUsable(ArgumentMatchers.anyString())).willReturn(false)

        val resultActions = mvc.perform(
            RestDocumentationRequestBuilders.put("/boards")
                .content(body)
                .header(accessTokenHeader, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `게시물 권한이 있어 수정에 성공한다`() {
        val boardUpdateDTO = TestFixture.boardUpdateDTO(
            savedBoardId = 1L,
            savedMemberId = 1L,
        )
        val token = "header.payload.sign"
        val accessTokenHeader = "access-token"
        val body = ObjectMapper().writeValueAsString(boardUpdateDTO)
        BDDMockito.given(jwtService.isUsable(ArgumentMatchers.anyString())).willReturn(true)

        val resultActions = mvc.perform(
            RestDocumentationRequestBuilders.put("/boards")
                .content(body)
                .header(accessTokenHeader, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "board-update-success",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("access-token").description("로그인 시 발급된 토큰")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("id").description("글번호"),
                        PayloadDocumentation.fieldWithPath("memberId").description("회원번호"),
                        PayloadDocumentation.fieldWithPath("title").description("제목"),
                        PayloadDocumentation.fieldWithPath("content").description("본문"),
                        PayloadDocumentation.fieldWithPath("writer").description("작성자")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("message").description("메시지"),
                        PayloadDocumentation.fieldWithPath("responseData").description("반환값"),
                        PayloadDocumentation.fieldWithPath("errorMessage").description("에러 메시지")
                    )
                )
            )
    }

    @Test
    fun `게시물 권한이 있어 삭제에 성공한다`() {
        val boardDeleteDTO = TestFixture.boardDeleteDTO(
            savedBoardId = 1L,
            savedMemberId = 1L,
        )
        val token = "header.payload.sign"
        val accessTokenHeader = "access-token"
        val body = ObjectMapper().writeValueAsString(boardDeleteDTO)
        BDDMockito.given(jwtService.isUsable(ArgumentMatchers.anyString())).willReturn(true)

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.delete("/boards")
                .content(body)
                .header(accessTokenHeader, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "board-delete-success",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("access-token").description("로그인 시 발급된 토큰")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("id").description("글번호"),
                        PayloadDocumentation.fieldWithPath("memberId").description("회원번호")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("message").description("메시지"),
                        PayloadDocumentation.fieldWithPath("responseData").description("반환값"),
                        PayloadDocumentation.fieldWithPath("errorMessage").description("에러 메시지")
                    )
                )
            )
    }
}