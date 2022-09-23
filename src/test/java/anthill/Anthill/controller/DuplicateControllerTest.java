package anthill.Anthill.controller;

import anthill.Anthill.api.controller.DuplicateController;
import anthill.Anthill.api.dto.member.MemberDuplicateResponseDTO;
import anthill.Anthill.api.service.JwtService;
import anthill.Anthill.api.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureRestDocs
@WebMvcTest(DuplicateController.class)
class DuplicateControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MemberService memberService;

    @Test
    public void 닉네임_중복되지않는_테스트() throws Exception {
        //given
        boolean result = false;
        String nickName = "testNickName";
        given(memberService.checkNicknameDuplicate(nickName)).willReturn(result);


        //when
        ResultActions resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-nickname/{nickname}", nickName));


        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("nick-name-non-duplicate",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("nickname").description("닉네임")
                        ),

                        responseFields(
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("responseData").description("반환값"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )

                ));

    }

    @Test
    public void 닉네임_중복_테스트() throws Exception {
        //given
        boolean result = true;
        String nickName = "testNickName";
        given(memberService.checkNicknameDuplicate(nickName)).willReturn(result);

        //when
        ResultActions resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-nickname/{nickname}", nickName));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("nick-name-duplicate",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("nickname").description("닉네임")
                        ),

                        responseFields(
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("responseData").description("반환값"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )
                ));

    }


    @Test
    public void 아이디_중복되지않는_테스트() throws Exception {
        //given
        boolean result = false;
        String userId = "testUserId";
        given(memberService.checkUserIdDuplicate(userId)).willReturn(result);


        //when
        ResultActions resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-id/{userId}", userId));


        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("user-id-non-duplicate",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("아이디")
                        ),

                        responseFields(
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("responseData").description("반환값"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )

                ));

    }

    @Test
    public void 아이디_중복_테스트() throws Exception {
        //given
        boolean result = true;
        String userId = "testUserId";
        given(memberService.checkUserIdDuplicate(userId)).willReturn(result);


        //when
        ResultActions resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-id/{userId}", userId));


        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("user-id-duplicate",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("아이디")
                        ),

                        responseFields(
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("responseData").description("반환값"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )

                ));

    }

    @Test
    public void 전화번호_중복_테스트() throws Exception {
        //given
        boolean result = false;
        String phoneNumber = "01012345678";
        given(memberService.checkPhoneNumberDuplicate(phoneNumber)).willReturn(result);


        //when
        ResultActions resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-phone-number/{phone-number}", phoneNumber));


        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("user-phone-number-non-duplicate",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("phone-number").description("전화번호")
                        ),

                        responseFields(
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("responseData").description("반환값"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )

                ));

    }

    @Test
    public void 전화번호_중복되지않는_테스트() throws Exception {
        //given
        boolean result = true;
        String phoneNumber = "01012345678";
        given(memberService.checkPhoneNumberDuplicate(phoneNumber)).willReturn(result);


        //when
        ResultActions resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/user-phone-number/{phone-number}", phoneNumber));


        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("user-phone-number-duplicate",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("phone-number").description("전화번호")
                        ),

                        responseFields(
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("responseData").description("반환값"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )

                ));

    }

    private MemberDuplicateResponseDTO makeMemberDuplicateResponseDTO(boolean result) {
        return MemberDuplicateResponseDTO.builder()
                                         .message(String.valueOf(result))
                                         .build();
    }

}