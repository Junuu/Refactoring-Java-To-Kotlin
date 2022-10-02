package anthill.Anthill.api.controller

import anthill.Anthill.api.service.JwtService
import anthill.Anthill.api.service.MemberService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureRestDocs
@WebMvcTest(DuplicateController::class)
class DuplicateControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var jwtService: JwtService

    @MockBean
    lateinit var memberService: MemberService

    @Test
    fun `닉네임이 중복되지 않는 경우 false를 반환한다`() {
        val result = false
        val nickName = "testNickName"
        BDDMockito.given(memberService.checkNicknameDuplicate(nickName)).willReturn(result)

        val resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-nickname/{nickname}", nickName))

        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "nick-name-non-duplicate",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("nickname").description("닉네임")
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
    fun `닉네임이 중복되지 않는 경우 true를 반환한다`() {
        val result = true
        val nickName = "testNickName"
        BDDMockito.given(memberService.checkNicknameDuplicate(nickName)).willReturn(result)

        val resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-nickname/{nickname}", nickName))

        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "nick-name-duplicate",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("nickname").description("닉네임")
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
    fun `아이디가 중복되는 경우 false를 반환한다`() {
        val result = false
        val userId = "testUserId"
        BDDMockito.given(memberService.checkUserIdDuplicate(userId)).willReturn(result)

        val resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-id/{userId}", userId))

        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "user-id-non-duplicate",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("userId").description("아이디")
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
    fun `아이디가 중복되지 않는 경우 true를 반환한다`() {
        val result = true
        val userId = "testUserId"
        BDDMockito.given(memberService.checkUserIdDuplicate(userId)).willReturn(result)

        val resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-id/{userId}", userId))

        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "user-id-duplicate",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("userId").description("아이디")
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
    fun `전화번호가 중복되는 경우 false를 반환한다`() {
        val result = false
        val phoneNumber = "01012345678"
        BDDMockito.given(memberService.checkPhoneNumberDuplicate(phoneNumber)).willReturn(result)

        val resultActions =
            mvc.perform(RestDocumentationRequestBuilders.get("/user-phone-number/{phone-number}", phoneNumber))

        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "user-phone-number-non-duplicate",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("phone-number").description("전화번호")
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
    fun `전화번호가 중복되지 않는 경우 true를 반환한다`() {
        val result = true
        val phoneNumber = "01012345678"
        BDDMockito.given(memberService.checkPhoneNumberDuplicate(phoneNumber)).willReturn(result)

        val resultActions =
            mvc.perform(RestDocumentationRequestBuilders.get("/user-phone-number/{phone-number}", phoneNumber))

        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "user-phone-number-duplicate",
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("phone-number").description("전화번호")
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