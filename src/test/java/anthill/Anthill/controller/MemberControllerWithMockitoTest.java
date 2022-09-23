package anthill.Anthill.controller;

import anthill.Anthill.api.controller.MemberController;
import anthill.Anthill.api.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerWithMockitoTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                                 .build();
    }

    @Test
    @DisplayName("Hello Test")
    public void returnOkMessage() throws Exception {
        //given
        String ok = "ok";

        //when
        ResultActions resultActions = mockMvc.perform(get("/members"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(ok));
    }

}
