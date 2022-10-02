package anthill.Anthill.controller;

import anthill.Anthill.api.controller.MemberController;
import anthill.Anthill.db.domain.member.Address;
import anthill.Anthill.api.dto.member.MemberLoginRequestDTO;
import anthill.Anthill.api.dto.member.MemberRequestDTO;
import anthill.Anthill.api.dto.member.MemberResponseDTO;
import anthill.Anthill.api.service.JwtService;
import anthill.Anthill.api.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import javax.naming.AuthenticationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtService jwtService;


    @Test
    public void 회원가입_입력값_유효하지않음_테스트() throws Exception {
        //given
        MemberRequestDTO memberRequestDTO = MemberRequestDTO.builder()
                                                            .build();
        String body = (new ObjectMapper()).writeValueAsString(memberRequestDTO);

        //when
        ResultActions resultActions = mvc.perform(post("/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_입력값_유효_테스트() throws Exception {
        //given
        MemberRequestDTO memberRequestDTO = getMemberRequestDTO();
        String body = (new ObjectMapper()).writeValueAsString(memberRequestDTO);

        //when
        ResultActions resultActions = mvc.perform(post("/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isCreated());
    }

    @Test
    public void 회원_중복발생_테스트() throws Exception {
        //given
        MemberRequestDTO memberRequestDTO = getMemberRequestDTO();
        String body = (new ObjectMapper()).writeValueAsString(memberRequestDTO);
        boolean duplicateResult = true;
        given(memberService.join(any())).willThrow(new IllegalArgumentException());

        //when
        ResultActions resultActions = mvc.perform(post("/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void 회원_중복되지않음_테스트() throws Exception {
        //given
        MemberRequestDTO memberRequestDTO = getMemberRequestDTO();
        String body = (new ObjectMapper()).writeValueAsString(memberRequestDTO);
        given(memberService.join(any())).willReturn(1L);

        //when
        ResultActions resultActions = mvc.perform(post("/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isCreated())
                .andDo(document("member-join-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").description("아이디"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("nickName").description("닉네임"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("phoneNumber").description("전화 번호"),
                                fieldWithPath("address.streetNameAddress").description("주소")
                                                                 .optional(),
                                fieldWithPath("address.detailAddress").description("상세 주소")
                                                                 .optional(),
                                fieldWithPath("address.zipCode").description("우편 번호")
                                                                .optional()
                        ),
                        responseFields(
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("responseData").description("반환값"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )
                ));
    }

    @Test
    public void 로그인_성공_테스트() throws Exception {
        //given
        MemberLoginRequestDTO memberLoginRequestDTO = getMemberLoginRequestDto();
        String body = (new ObjectMapper()).writeValueAsString(memberLoginRequestDTO);
        String token = "header.payload.verifySignature";
        boolean loginResult = true;
        given(memberService.login(any())).willReturn(loginResult);
        given(jwtService.create(any(), any(), any())).willReturn(token);

        //when
        ResultActions resultActions = mvc.perform(post("/members/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().string("access-token", token))
                .andDo(document("member-login-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").description("아이디"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("access-token").description("로그인 시 발급된 토큰")
                        ),
                        responseFields(
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("responseData").description("반환값"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )
                ));
    }

    @Test
    public void 로그인_실패_테스트() throws Exception {
        //given
        MemberLoginRequestDTO memberLoginRequestDTO = getMemberLoginRequestDto();
        String body = (new ObjectMapper()).writeValueAsString(memberLoginRequestDTO);
        given(memberService.login(any())).willThrow(new IllegalStateException());

        //when
        ResultActions resultActions = mvc.perform(post("/members/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 회원조회_성공_테스트() throws Exception {
        //given
        MemberResponseDTO memberResponseDTO = getMemberResponseDTO();
        given(memberService.findByUserID(any())).willReturn(memberResponseDTO);
        //when
        ResultActions resultActions = mvc.perform(RestDocumentationRequestBuilders.get("/members/{userid}", memberResponseDTO.getUserId()));
        //then
        resultActions.andExpect(status().isOk())
                     .andDo(document("member-get-by-id-success",
                             preprocessResponse(prettyPrint()),
                             pathParameters(
                                     parameterWithName("userid").description("아이디")
                             ),
                             responseFields(
                                     fieldWithPath("message").description("메시지"),
                                     fieldWithPath("responseData").description("반환값"),
                                     fieldWithPath("responseData.userId").description("아이디"),
                                     fieldWithPath("responseData.name").description("이름"),
                                     fieldWithPath("responseData.nickName").description("닉네임"),
                                     fieldWithPath("responseData.phoneNumber").description("전화 번호"),
                                     fieldWithPath("responseData.address.streetNameAddress").description("주소")
                                                                                   .optional(),
                                     fieldWithPath("responseData.address.detailAddress").description("상세 주소")
                                                                                   .optional(),
                                     fieldWithPath("responseData.address.zipCode").description("우편 번호")
                                                                                  .optional(),
                                     fieldWithPath("errorMessage").description("에러 메시지")

                             )
                     ));

    }

    @Test
    public void 회원조회_실패_테스트() throws Exception {
        //given
        MemberResponseDTO memberResponseDTO = getMemberResponseDTO();
        given(memberService.findByUserID(any())).willThrow(new IllegalArgumentException());
        //when
        ResultActions resultActions = mvc.perform(get("/members/" + "test"));
        //then
        resultActions.andExpect(status().isNotFound());

    }


    private MemberResponseDTO getMemberResponseDTO() {
        Address myAddress = Address.builder()
                                   .streetNameAddress("경기도 시흥시")
                .detailAddress("XX아파트 XX호")
                                   .zipCode("429-010")
                                   .build();

        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                                                               .userId("test")
                                                               .name("test")
                                                               .nickName("test")
                                                               .phoneNumber("01012345678")
                                                               .address(myAddress)
                                                               .build();
        return memberResponseDTO;
    }


    private MemberRequestDTO getMemberRequestDTO() {

        Address myAddress = Address.builder()
                                   .streetNameAddress("경기도 시흥시")
                .detailAddress("XX아파트 XX호")
                                   .zipCode("429-010")
                                   .build();

        MemberRequestDTO memberRequestDTO = MemberRequestDTO.builder()
                                                            .userId("junwooKim")
                                                            .name("KIM")
                                                            .nickName("junuuu")
                                                            .password("123456789")
                                                            .phoneNumber("01012345678")
                                                            .address(myAddress)
                                                            .build();
        return memberRequestDTO;
    }

    private MemberLoginRequestDTO getMemberLoginRequestDto() {
        MemberLoginRequestDTO memberLoginRequestDTO = MemberLoginRequestDTO.builder()
                                                                           .userId("test")
                                                                           .password("123456789")
                                                                           .build();
        return memberLoginRequestDTO;
    }

}