package anthill.Anthill.api.controller

import TestFixture
import anthill.Anthill.api.dto.member.MemberRequestDTO
import anthill.Anthill.api.service.JwtService
import anthill.Anthill.api.service.MemberService
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
@WebMvcTest(MemberController::class)
class MemberControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var memberService: MemberService

    @MockBean
    lateinit var jwtService: JwtService

    @Test
    fun `회원가입 입력값이 유효하지않으면 400 BAD Request 반환`() {
        val memberRequestDTO = TestFixture.memberRequestDTO(
            userId = ""
        )
        val body = ObjectMapper().writeValueAsString(memberRequestDTO)

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `회원가입시 입력값이 유효성 검사를 통과하면 201 Created 반환`() {
        val memberRequestDTO = TestFixture.memberRequestDTO()
        val body = ObjectMapper().writeValueAsString(memberRequestDTO)

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `회원가입 시 중복이 발생한다면 404 not found 반환`() {
        val memberRequestDTO = TestFixture.memberRequestDTO()
        val body = ObjectMapper().writeValueAsString(memberRequestDTO)
        BDDMockito.given(memberService.join(ArgumentMatchers.any())).willThrow(IllegalArgumentException())

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `회원가입 시 중복이 발생지 않으면 201 Create 반환`() {
        val memberRequestDTO = TestFixture.memberRequestDTO()
        val body = ObjectMapper().writeValueAsString(memberRequestDTO)
        BDDMockito.given(memberService.join(ArgumentMatchers.any())).willReturn(1L)

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(
                MockMvcRestDocumentation.document(
                    "member-join-success",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("userId").description("아이디"),
                        PayloadDocumentation.fieldWithPath("name").description("이름"),
                        PayloadDocumentation.fieldWithPath("nickName").description("닉네임"),
                        PayloadDocumentation.fieldWithPath("password").description("비밀번호"),
                        PayloadDocumentation.fieldWithPath("phoneNumber").description("전화 번호"),
                        PayloadDocumentation.fieldWithPath("address.streetNameAddress").description("주소")
                            .optional(),
                        PayloadDocumentation.fieldWithPath("address.detailAddress").description("상세 주소")
                            .optional(),
                        PayloadDocumentation.fieldWithPath("address.zipCode").description("우편 번호")
                            .optional()
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
    fun `로그인 성공시 200 OK 반환`() {
        val memberLoginRequestDTO = TestFixture.memberLoginRequestDTO(
            password = "123456789"
        )
        val body = ObjectMapper().writeValueAsString(memberLoginRequestDTO)
        val token = "header.payload.verifySignature"
        val loginResult = true
        BDDMockito.given(memberService.login(ArgumentMatchers.any())).willReturn(loginResult)
        BDDMockito.given(jwtService.create(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
            .willReturn(token)

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/members/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.header().string("access-token", token))
            .andDo(
                MockMvcRestDocumentation.document(
                    "member-login-success",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("userId").description("아이디"),
                        PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                    ),
                    HeaderDocumentation.responseHeaders(
                        HeaderDocumentation.headerWithName("access-token").description("로그인 시 발급된 토큰")
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
    fun `로그인 실패시 401 Unauthorized 반환`() {
        val memberLoginRequestDTO = TestFixture.memberLoginRequestDTO(
            password = "123456789"
        )
        val body = ObjectMapper().writeValueAsString(memberLoginRequestDTO)
        BDDMockito.given(memberService.login(ArgumentMatchers.any())).willThrow(IllegalStateException())

        val resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/members/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        resultActions
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `회원 단건 조회 성공시 200 OK 반환`() {
        val memberResponseDTO = TestFixture.memberResponseDTO()
        BDDMockito.given(memberService.findByUserID(ArgumentMatchers.any())).willReturn(memberResponseDTO)

        val resultActions =
            mvc.perform(RestDocumentationRequestBuilders.get("/members/{userid}", memberResponseDTO.userId))

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "member-get-by-id-success",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("userid").description("아이디")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("message").description("메시지"),
                        PayloadDocumentation.fieldWithPath("responseData").description("반환값"),
                        PayloadDocumentation.fieldWithPath("responseData.userId").description("아이디"),
                        PayloadDocumentation.fieldWithPath("responseData.name").description("이름"),
                        PayloadDocumentation.fieldWithPath("responseData.nickName").description("닉네임"),
                        PayloadDocumentation.fieldWithPath("responseData.phoneNumber").description("전화 번호"),
                        PayloadDocumentation.fieldWithPath("responseData.address.streetNameAddress").description("주소")
                            .optional(),
                        PayloadDocumentation.fieldWithPath("responseData.address.detailAddress").description("상세 주소")
                            .optional(),
                        PayloadDocumentation.fieldWithPath("responseData.address.zipCode").description("우편 번호")
                            .optional(),
                        PayloadDocumentation.fieldWithPath("errorMessage").description("에러 메시지")
                    )
                )
            )
    }

    @Test
    fun `회원 단건 조회 실패시 404 Not Found 반환`() {
        BDDMockito.given(memberService.findByUserID(ArgumentMatchers.any())).willThrow(IllegalArgumentException())

        val resultActions = mvc.perform(MockMvcRequestBuilders.get("/members/" + "test"))

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

}